package pt.ulusofona.deisi.aed.deisiflix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Reader {
    static boolean DEBUG = false;

    /* Reader functions */
    /**
     * Reads movie entries from a file, stores them in appropriate data structures and returns those data structures.
     * @param filePath The path of the file to read
     * @return Returns a 'MoviesData' object with all the data structures that were created
     * @throws IOException Exceptions related to file reading
     */
    public static MoviesData movieReader(String filePath) throws IOException {
        long moviesTimerStart = System.currentTimeMillis();

        FileReader fr = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(fr);

        ArrayList<Filme> moviesFileOrder = new ArrayList<Filme>();  // Movies with file order preserved
        HashMap<Integer, Filme> moviesDict = new HashMap<>();  // Movies by ID (KEY: Movie ID, VALUE: 'Filme' object)
        HashMap<Integer, ArrayList<Integer>> movieIDsByYear = new HashMap<>();  // Movies ID by Year
        ArrayList<String> ignoredLines = new ArrayList<String>(); // Ignored Lines
        String line = null;

        while ((line = reader.readLine()) != null) {
            String[] components = line.split(",");

            if (components.length == 5) {
                int id = Integer.parseInt(components[0].strip());
                String title = components[1].strip();
                float duration = Float.parseFloat(components[2].strip());
                int budget = Integer.parseInt(components[3].strip());
                String date = components[4].strip();

                Filme movie = new Filme();
                movie.id = id;
                movie.titulo = title;
                movie.orcamento = budget;
                movie.dataLancamento = date;

                moviesFileOrder.add(movie);

                // Gets movie year
                int year = Integer.parseInt(date.split("-")[2]);

                // Adds 'id' to the correspondent Year in 'movieIDsByYear'
                // Checks if 'movieIDsByYear' already that year
                if (movieIDsByYear.containsKey(year)) {
                    // Adds 'id' to the correspondent year in the HashMap
                    movieIDsByYear.get(year).add(id);
                } else {
                    // Creates new HashMap entry with the current 'year' and add 'id' to it
                    movieIDsByYear.put(year, new ArrayList<>());
                    movieIDsByYear.get(year).add(id);
                }

                // Adds every movie to 'moviesDict' HashMap (KEY: Movie Year, VALUE: 'Filme' object)
                if (!moviesDict.containsKey(id)) {
                    moviesDict.put(id, movie);
                }
            } else {
                ignoredLines.add(line);
            }
        }
        reader.close();

        long moviesTimerEnd = System.currentTimeMillis();
        if (DEBUG) {
            System.out.println("TIMER (moviesReader) -> " + (moviesTimerEnd - moviesTimerStart) + " ms");
        }

        // Returns 'MoviesData' object
        return new MoviesData(moviesFileOrder, moviesDict, movieIDsByYear, ignoredLines);
    }

    /**
     * Reads votes related entries from a file, stores them in appropriate data structures and returns those.
     * @param filePath The path of the file to read
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns an 'ArrayList' with all the ignored lines
     * @throws IOException Exceptions related to file reading
     */
    public static ArrayList<String> movieVotesReader(
            String filePath, HashMap<Integer, Filme> moviesDict
    ) throws IOException {
        long votesTimerStart = System.currentTimeMillis();

        FileReader fr = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(fr);

        ArrayList<String> ignoredLines = new ArrayList<String>();
        String line = null;

        while ((line = reader.readLine()) != null) {
            String[] components = line.split(",");

            if (components.length == 3) {
                int id = Integer.parseInt(components[0].strip());
                double votesAverage = Double.parseDouble(components[1].strip());
                int votesTotal = Integer.parseInt(components[2].strip());

                // Adds votes' data to correspondent 'Filme' object in 'moviesDict' for each line in the file
                if (moviesDict.containsKey(id)) {
                    // Gets 'Filme' object for current movie 'id'
                    Filme movie = moviesDict.get(id);

                    // Adds votes' data
                    movie.mediaVotos = votesAverage;
                    movie.nrVotos = votesTotal;
                }
            } else {
                ignoredLines.add(line);
            }
        }
        reader.close();

        long votesTimerEnd = System.currentTimeMillis();
        if (DEBUG) {
            System.out.println("TIMER (votesReader) -> " + (votesTimerEnd - votesTimerStart) + " ms");
        }

        return ignoredLines;
    }

    /**
     * Reads people entries from a file, stores them in appropriate data structures and returns those.
     * @param filePath The path of the file to read
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns a 'PeopleData' object with all the data structures that were created
     * @throws IOException Exceptions related to file reading
     */
    public static PeopleData peopleReader(String filePath, HashMap<Integer, Filme> moviesDict) throws IOException {
        long peopleTimerStart = System.currentTimeMillis();

        FileReader fr = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(fr);

        HashMap<String, ArrayList<MovieAssociate>> moviesPeople = new HashMap<>();  // HashMap with all people
        HashMap<Integer, String> actorsByID = new HashMap<>();  // All actors by ID (KEY: actor ID, VALUE: actor name)
        ArrayList<String> ignoredLines = new ArrayList<String>();  // Ignored Lines
        HashMap<Integer, ArrayList<String>> duplicateLinesByYear = new HashMap<>();  // Duplicate lines by year
        String line = null;

        int currentLineNum = 1;  // Stores current line number in the file

        while ((line = reader.readLine()) != null) {
            String[] components = line.split(",");

            if (components.length == 5) {
                String type = components[0].strip();
                int idPerson = Integer.parseInt(components[1].strip());
                String name = components[2].strip();
                char gender = components[3].strip().charAt(0);
                int idMovie = Integer.parseInt(components[4].strip());

                // Creates new 'Pessoa' Object with the person data
                Pessoa person = new Pessoa(idPerson, name, gender);

                // Adds PERSON to 'moviesDict' HashMap
                ReaderFunctions.addPersonToMovies(person, type, idMovie, moviesDict);

                // Creates new instance of 'MovieAssociate' to store in 'moviesPeople'
                MovieAssociate personMovieAssociate = new MovieAssociate();
                personMovieAssociate.id = idPerson;
                personMovieAssociate.name = name;
                personMovieAssociate.gender = gender;
                personMovieAssociate.type = type;
                personMovieAssociate.associatedMoviesID = new ArrayList<>();
                personMovieAssociate.associatedMoviesID.add(idMovie);

                // Adds 'MovieAssociate' to 'moviesPeople'
                ReaderFunctions.addMovieAssociateToPeople(
                        personMovieAssociate,
                        moviesPeople,
                        currentLineNum,
                        duplicateLinesByYear,
                        moviesDict
                );

                // Adds actor to 'actorsByID'
                if (type.equals("ACTOR") && !actorsByID.containsKey(idPerson)) {
                    actorsByID.put(idPerson, name);
                }

            } else {
                ignoredLines.add(line);
            }
            currentLineNum++;
        }

        reader.close();
        long peopleTimerEnd = System.currentTimeMillis();
        if (DEBUG) {
            System.out.println("TIMER (peopleReader) -> " + (peopleTimerEnd - peopleTimerStart) + " ms");
        }

        return new PeopleData(moviesPeople, actorsByID, duplicateLinesByYear, ignoredLines);
    }

    /**
     * Reads genre entries from a file, stores them in appropriate data structures and returns those
     * @param filePath The path of the file to read
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns an 'ArrayList' with all the ignored lines
     * @throws IOException Exceptions related to file reading
     */
    public static ArrayList<String> genresReader(
            String filePath, HashMap<Integer, Filme> moviesDict
    ) throws IOException {
        long genresTimerStart = System.currentTimeMillis();

        FileReader fr = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(fr);

        ArrayList<String> ignoredLines = new ArrayList<String>();
        String line = null;

        while ((line = reader.readLine()) != null) {
            String[] components = line.split(",");

            if (components.length == 2) {
                String genre = components[0].strip();
                int id = Integer.parseInt(components[1].strip());

                /* Adds genre to the correspondent movieID in 'moviesDict' */
                // Creates new 'GeneroCinematografico'
                GeneroCinematografico newGenre = new GeneroCinematografico(genre);

                // Checks if movie with 'id' ID exists
                if (moviesDict.containsKey(id)) {
                    // Gets current 'Filme' object
                    Filme movie = moviesDict.get(id);

                    // Checks if 'generos' exists, if not, creates one
                    if (movie.generos != null) {
                        movie.generos.add(newGenre);
                    } else {
                        ArrayList<GeneroCinematografico> movieGenres = new ArrayList<>();
                        movieGenres.add(newGenre);
                        movie.generos = movieGenres;
                    }
                }
            } else {
                ignoredLines.add(line);
            }
        }
        reader.close();

        long genresTimerEnd = System.currentTimeMillis();
        if (DEBUG) {
            System.out.println("TIMER (genresReader) -> " + (genresTimerEnd - genresTimerStart) + " ms");
        }

        return ignoredLines;
    }
}
