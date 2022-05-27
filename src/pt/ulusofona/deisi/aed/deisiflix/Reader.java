package pt.ulusofona.deisi.aed.deisiflix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Reader {
    static boolean DEBUG = false;

    // Local Database Paths
    // static String shortMovies = "test-files/deisi_movies_short.txt";
    static String largeMovies = "test-files/deisi_movies_large.txt";
    // static String shortVotes = "test-files/deisi_movie_votes_short.txt";
    static String largeVotes = "test-files/deisi_movie_votes_large.txt";
    // static String shortPeople = "test-files/deisi_people_short.txt";
    static String largePeople = "test-files/deisi_people_large.txt";
    // static String shortGenres = "test-files/deisi_genres_short.txt";
    static String largeGenres = "test-files/deisi_genres_large.txt";

    // Reader functions
    public static MoviesData movieReader() throws IOException {
        FileReader fr = new FileReader(largeMovies);
        BufferedReader reader = new BufferedReader(fr);

        ArrayList<Filme> movies = new ArrayList<Filme>();  // Movies
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

                movies.add(movie);

                if (DEBUG) {
                    System.out.println("ID: " + id);
                    System.out.println("Title: "+ title);
                    System.out.println("Duration: " + duration);
                    System.out.println("Budget " + budget);
                    System.out.println("Date: " + date);
                }
            } else {
                ignoredLines.add(line);
            }
        }
        reader.close();

        // Returns 'MoviesData' object
        return new MoviesData(movies, ignoredLines);

        /*
            TODO:
             -Store movies in an arrayList keeping the same file order (already done)
             -Store movies in a sorted (by ID) arrayList so its easier to access through (binary search)

             Problems:
             -Double the memory size
         */
    }

    // Return 'ignoredLines'
    public static ArrayList<String> movieVotesReader(ArrayList<Filme> movies) throws IOException {
        FileReader fr = new FileReader(largeVotes);
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

                // Adds vote data to correspondent 'Filme' object in 'movies'
                // TODO: Improve this. This is not efficient AT ALL.
                for (Filme movie : movies) {
                    if (movie.id == id) {
                        movie.mediaVotos = votesAverage;
                        movie.nrVotos = votesTotal;
                    }
                }
            } else {
                ignoredLines.add(line);
            }
        }

        reader.close();
        return ignoredLines;
    }

    // Returns 'ignoredLines'
    public static ArrayList<String> peopleReader(HashMap<String, MovieAssociate> moviesPeople) throws IOException {
        // long startTimer = System.currentTimeMillis();
        FileReader fr = new FileReader(largePeople);
        BufferedReader reader = new BufferedReader(fr);

        ArrayList<String> ignoredLines = new ArrayList<String>();
        String line = null;

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

                // If KEY does not exist create one, otherwise add movie ID to 'associatedMoviesID'
                if (!moviesPeople.containsKey(name)) {
                    // Creates new 'MovieAssociate' instance to store the person info
                    MovieAssociate person = new MovieAssociate();
                    person.id = idPerson;
                    person.name = name;
                    person.gender = gender;
                    person.type = type;
                    // Creates a new ArrayList to store 'associateMoviesID'
                    ArrayList<Integer> associatedMoviesID = new ArrayList<>();
                    associatedMoviesID.add(idMovie);
                    person.associatedMoviesID = associatedMoviesID;

                    // Adds people to an HashMap (KEY -> Person name, VALUE -> MovieAssociate)
                    moviesPeople.put(name, person);
                } else {
                    // Adds 'idMovie' to 'associateMoviesID'
                    MovieAssociate updatedValue = moviesPeople.get(name);
                    updatedValue.associatedMoviesID.add(idMovie);
                    moviesPeople.put(name, updatedValue);
                }

            } else {
                ignoredLines.add(line);
            }

            // TODO: Add to main movies 'ArrayList'
        }

        reader.close();
        // long endTimer = System.currentTimeMillis();
        // System.out.println("TIMER -> peopleReader: " + (endTimer - startTimer));
        return ignoredLines;
    }

    // Return 'ignoredLines'
    public static ArrayList<String> genresReader() throws IOException {
        FileReader fr = new FileReader(largeGenres);
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
            } else {
                ignoredLines.add(line);
            }

            // TODO: Add to class
        }

        reader.close();
        return ignoredLines;
    }
}
