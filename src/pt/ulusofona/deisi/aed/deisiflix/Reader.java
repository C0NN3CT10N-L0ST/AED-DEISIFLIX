package pt.ulusofona.deisi.aed.deisiflix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Reader {
    static boolean DEBUG = false;
    static boolean DP = false;  // Sets Readers files' variables based on DP submission or Local environment

    // Reaers files
    static String moviesFile = "deisi_movies.txt";
    static String votesFile = "deisi_movie_votes.txt";
    static String peopleFile = "deisi_people.txt";
    static String genresFile = "deisi_genres.txt";

    // Local Database Paths
    // static String shortMovies = "test-files/deisi_movies_short.txt";
    static String largeMovies = "test-files/deisi_movies_large.txt";
    // static String shortVotes = "test-files/deisi_movie_votes_short.txt";
    static String largeVotes = "test-files/deisi_movie_votes_large.txt";
    // static String shortPeople = "test-files/deisi_people_short.txt";
    static String largePeople = "test-files/deisi_people_large.txt";
    // static String shortGenres = "test-files/deisi_genres_short.txt";
    static String largeGenres = "test-files/deisi_genres_large.txt";

    /* Reader functions */

    /**
     * Reads movie entries from a file, stores them in appropriate data structures and returns those data structures.
     * @return Returns a 'MoviesData' object with all the data structures that were created
     * @throws IOException Exceptions related to file reading
     */
    public static MoviesData movieReader() throws IOException {
        long moviesTimerStart = System.currentTimeMillis();
        if (!DP) {
            moviesFile = largeMovies;
        }
        FileReader fr = new FileReader(moviesFile);
        BufferedReader reader = new BufferedReader(fr);

        ArrayList<Filme> moviesFileOrder = new ArrayList<Filme>();  // Movies with file order preserved
        HashMap<Integer, Filme> moviesDict = new HashMap<>();  // Movies by ID (KEY: Movie ID, VALUE: 'Filme' object)
        HashMap<Integer, ArrayList<Integer>> movieIDsByYear = new HashMap<>();  // Movies ID by Year
        ArrayList<String> ignoredLines = new ArrayList<String>(); // Ignored Lines
        String line = null;

        while ((line = reader.readLine()) != null) {
            if (DEBUG) {
                System.out.println("-----------".repeat(6));
                System.out.println("Line -> " + line);
            }

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

                if (DEBUG) {
                    System.out.println("ID: " + id);
                    System.out.println("Title: "+ title);
                    System.out.println("Duration: " + duration);
                    System.out.println("Budget " + budget);
                    System.out.println("Date: " + date);
                }

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
        System.out.println("TIMER (moviesReader) -> " + (moviesTimerEnd - moviesTimerStart) + " ms");

        // Returns 'MoviesData' object
        return new MoviesData(moviesFileOrder, moviesDict, movieIDsByYear, ignoredLines);
    }

    /**
     * Reads votes related entries from a file, stores them in appropriate data structures and returns those.
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns an 'ArrayList' with all the ignored lines
     * @throws IOException Exceptions related to file reading
     */
    public static ArrayList<String> movieVotesReader(HashMap<Integer, Filme> moviesDict) throws IOException {
        long votesTimerStart = System.currentTimeMillis();
        if (!DP) {
            votesFile = largeVotes;
        }
        FileReader fr = new FileReader(votesFile);
        BufferedReader reader = new BufferedReader(fr);

        ArrayList<String> ignoredLines = new ArrayList<String>();
        String line = null;

        while ((line = reader.readLine()) != null) {
            if (DEBUG) {
                System.out.println("-----------".repeat(6));
                System.out.println("Line -> " + line);
            }

            String[] components = line.split(",");

            if (components.length == 3) {
                int id = Integer.parseInt(components[0].strip());
                float votesAverage = Float.parseFloat(components[1].strip());
                int votesTotal = Integer.parseInt(components[2].strip());

                if (DEBUG) {
                    System.out.println("ID: " + id);
                    System.out.println("Vote Average: "+ votesAverage);
                    System.out.println("Nr. Votes: " + votesTotal);
                }

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
        System.out.println("TIMER (votesReader) -> " + (votesTimerEnd - votesTimerStart) + " ms");

        return ignoredLines;
    }

    /**
     * Reads people entries from a file, stores them in appropriate data structures and returns those.
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns a 'PeopleData' object with all the data structures that were created
     * @throws IOException Exceptions related to file reading
     */
    public static PeopleData peopleReader(HashMap<Integer, Filme> moviesDict) throws IOException {
        long peopleTimerStart = System.currentTimeMillis();
        if (!DP) {
            peopleFile = largePeople;
        }
        FileReader fr = new FileReader(peopleFile);
        BufferedReader reader = new BufferedReader(fr);

        HashMap<String, MovieAssociate> moviesPeople = new HashMap<>();  // 'HashMap' to store people
        ArrayList<String> ignoredLines = new ArrayList<String>();  // Ignored Lines
        HashMap<Integer, ArrayList<String>> duplicateLinesByYear = new HashMap<>();  // Duplicate lines by year
        String line = null;

        int currentLineNum = 1;  // Stores current line number in the file

        while ((line = reader.readLine()) != null) {
            if (DEBUG) {
                System.out.println("-----------".repeat(6));
                System.out.println("Line -> " + line);
            }

            String[] components = line.split(",");

            if (components.length == 5) {
                String type = components[0].strip();
                int idPerson = Integer.parseInt(components[1].strip());
                String name = components[2].strip();
                char gender = components[3].strip().charAt(0);
                int idMovie = Integer.parseInt(components[4].strip());

                if (DEBUG) {
                    System.out.println("Type Person: " + type);
                    System.out.println("ID: " + idPerson);
                    System.out.println("Name: "+ name);
                    System.out.println("Gender: " + gender);
                    System.out.println("ID Movie: " + idMovie);
                }

                // Creates new 'Pessoa' Object with the person data
                Pessoa person = new Pessoa(idPerson, name, gender);

                // Adds PERSON to 'moviesDict' HashMap
                ReaderFunctions.addPersonToMovies(person, type, idMovie, moviesDict);

                // If KEY does not exist create one, otherwise add movie ID to 'associatedMoviesID'
                if (!moviesPeople.containsKey(name)) {
                    // Creates new 'MovieAssociate' instance to store the person info
                    MovieAssociate movieAssociate = new MovieAssociate();
                    movieAssociate.id = idPerson;
                    movieAssociate.name = name;
                    movieAssociate.gender = gender;
                    movieAssociate.type = type;

                    // Creates a new ArrayList to store 'associateMoviesID'
                    ArrayList<Integer> associatedMoviesID = new ArrayList<>();
                    associatedMoviesID.add(idMovie);
                    movieAssociate.associatedMoviesID = associatedMoviesID;

                    // Adds people to an HashMap (KEY -> Person name, VALUE -> MovieAssociate)
                    moviesPeople.put(name, movieAssociate);
                } else {
                    // Gets HashMap entry for current person
                    MovieAssociate movieAssociateEntry = moviesPeople.get(name);

                    // Checks if ID of the person matches
                    if (movieAssociateEntry.id == idPerson) {
                        // Checks if 'idMovie' is already in 'associatedMoviesID'
                        if (movieAssociateEntry.associatedMoviesID.contains(idMovie)) {
                            // Adds duplicate line to 'duplicateLinesByYear'
                            ReaderFunctions.addLineToDuplicateLinesByYear(
                                    currentLineNum, idMovie, idPerson, duplicateLinesByYear, moviesDict);
                        } else {
                            // Adds 'idMovie' to 'associateMoviesID'
                            movieAssociateEntry.associatedMoviesID.add(idMovie);
                            // TODO: see if there is a more efficient way to do this
                        }
                    }
                }
            } else {
                ignoredLines.add(line);
            }
            currentLineNum++;
        }

        reader.close();
        long peopleTimerEnd = System.currentTimeMillis();
        System.out.println("TIMER (peopleReader) -> " + (peopleTimerEnd - peopleTimerStart) + " ms");

        return new PeopleData(moviesPeople, duplicateLinesByYear, ignoredLines);
    }

    /**
     * Reads genre entries from a file, stores them in appropriate data structures and returns those
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns an 'ArrayList' with all the ignored lines
     * @throws IOException Exceptions related to file reading
     */
    public static ArrayList<String> genresReader(HashMap<Integer, Filme> moviesDict) throws IOException {
        long genresTimerStart = System.currentTimeMillis();
        if (!DP) {
            genresFile = largeGenres;
        }
        FileReader fr = new FileReader(genresFile);
        BufferedReader reader = new BufferedReader(fr);

        ArrayList<String> ignoredLines = new ArrayList<String>();
        String line = null;

        while ((line = reader.readLine()) != null) {
            if (DEBUG) {
                System.out.println("-----------".repeat(6));
                System.out.println("Line -> " + line);
            }

            String[] components = line.split(",");

            if (components.length == 2) {
                String genre = components[0].strip();
                int id = Integer.parseInt(components[1].strip());

                if (DEBUG) {
                    System.out.println("Genre Name: " + genre);
                    System.out.println("ID Movie: " + id);
                }

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
        System.out.println("TIMER (genresReader) -> " + (genresTimerEnd - genresTimerStart) + " ms");

        return ignoredLines;
    }
}
