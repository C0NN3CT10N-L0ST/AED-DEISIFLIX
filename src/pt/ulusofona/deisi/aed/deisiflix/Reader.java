import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {
    static boolean DEBUG = true;
    static String testDBPath = new File("").getAbsolutePath() + "\\src\\pt\\ulusofona\\deisi\\aed\\deisiflix\\testDB\\";

    public static ArrayList<Filme> movieReader() throws IOException {
        FileReader fr = new FileReader(testDBPath + "deisi_movies.txt");
        BufferedReader reader = new BufferedReader(fr);

        ArrayList<Filme> movies = new ArrayList<>();
        String line = null;

        while ((line = reader.readLine()) != null) {
            if (DEBUG) {
                System.out.println("Line -> " + line);
            }

            String components[] = line.split(",");

            if (components.length == 5) {
                int id = Integer.parseInt(components[0].strip());
                String title = components[1].strip();
                int duration = Integer.parseInt(components[2].strip());
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
                    System.out.println("-----------".repeat(6));
                }
            }
        }
        reader.close();
        return movies;
    }

    public static void movieVotesReader() throws IOException {
        FileReader fr = new FileReader(testDBPath + "deisi_movie_votes.txt");
        BufferedReader reader = new BufferedReader(fr);

        String line = null;

        while ((line = reader.readLine()) != null) {
            if (DEBUG) {
                System.out.println("Line -> " + line);
            }

            String components[] = line.split(",");

            if (components.length == 3) {
                int id = Integer.parseInt(components[0].strip());
                float votesAverage = Float.parseFloat(components[1].strip());
                int votesTotal = Integer.parseInt(components[2].strip());

                if (DEBUG) {
                    System.out.println("ID: " + id);
                    System.out.println("Vote Average: "+ votesAverage);
                    System.out.println("Nr. Votes: " + votesTotal);
                    System.out.println("-----------".repeat(6));
                }
            }

            // TODO: Add to class
        }

        reader.close();
    }

    public static void peopleReader() throws IOException {
        FileReader fr = new FileReader(testDBPath + "deisi_actors.txt");
        BufferedReader reader = new BufferedReader(fr);

        String line = null;

        while ((line = reader.readLine()) != null) {
            if (DEBUG) {
                System.out.println("Line -> " + line);
            }

            String components[] = line.split(",");

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
                    System.out.println("-----------".repeat(6));
                }
            }

            // TODO: Add to class
        }

        reader.close();
    }

    public static void genresReader() throws IOException {
        FileReader fr = new FileReader(testDBPath + "deisi_genres.txt");
        BufferedReader reader = new BufferedReader(fr);

        String line = null;

        while ((line = reader.readLine()) != null) {
            if (DEBUG) {
                System.out.println("Line -> " + line);
            }

            String components[] = line.split(",");

            if (components.length == 2) {
                String genre = components[0].strip();
                int id = Integer.parseInt(components[1].strip());

                if (DEBUG) {
                    System.out.println("Genre Name: " + genre);
                    System.out.println("ID Movie: " + id);
                    System.out.println("-----------".repeat(6));
                }
            }

            // TODO: Add to class
        }

        reader.close();
    }

    public static ArrayList<String> getIgnoredLines(String fileName, int numberOfComponents) throws IOException {
        FileReader fr = new FileReader(testDBPath + fileName);
        BufferedReader reader = new BufferedReader(fr);

        ArrayList<String> ignoredLines = new ArrayList<>();

        String line = null;

        while ((line = reader.readLine()) != null) {
            if (DEBUG) {
                System.out.println("Line -> " + line);
            }

            String components[] = line.split(",");

            if (components.length != numberOfComponents) {
                ignoredLines.add(line);
            }

        }

        reader.close();

        return ignoredLines;
    }
}
