package pt.ulusofona.deisi.aed.deisiflix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {
    static boolean DEBUG = true;

    // Local Database Paths
    static String shortMovies = "test-files/deisi_movies_short.txt";
    static String largeMovies = "test-files/deisi_movies_large.txt";
    static String shortVotes = "test-files/deisi_movie_votes_short.txt";
    static String largeVotes = "test-files/deisi_movie_votes_large.txt";
    static String shortPeople = "test-files/deisi_people_short.txt";
    static String largePeople = "test-files/deisi_people_large.txt";
    static String shortGenres = "test-files/deisi_genres_short.txt";
    static String largeGenres = "test-files/deisi_genres_large.txt";

    // Reader functions
    public static MoviesData movieReader() throws IOException {
        FileReader fr = new FileReader(shortMovies);
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
    }

    // In part1 we only need to return 'ignoredLines'
    public static ArrayList<String> movieVotesReader() throws IOException {
        FileReader fr = new FileReader(shortVotes);
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
            } else {
                ignoredLines.add(line);
            }

            // TODO: Add to class
        }

        reader.close();
        return ignoredLines;
    }

    // In part1 we only need to return 'ignoredLines'
    public static ArrayList<String> peopleReader() throws IOException {
        FileReader fr = new FileReader(shortPeople);
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
                String tipo = components[0].strip();
                int idPerson = Integer.parseInt(components[1].strip());
                String name = components[2].strip();
                String genre = components[3].strip();
                int idMovie = Integer.parseInt(components[4].strip());

                if (DEBUG) {
                    System.out.println("Tipe Person: " + tipo);
                    System.out.println("ID: " + idPerson);
                    System.out.println("Name: "+ name);
                    System.out.println("Genre: " + genre);
                    System.out.println("ID Movie: " + idMovie);
                }
            } else {
                ignoredLines.add(line);
            }

            // TODO: Add to class
        }

        reader.close();
        return ignoredLines;
    }

    // In part1 we only need to return 'ignoredLines'
    public static ArrayList<String> genresReader() throws IOException {
        FileReader fr = new FileReader(shortGenres);
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
