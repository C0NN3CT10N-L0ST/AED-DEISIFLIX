package pt.ulusofona.deisi.aed.deisiflix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    // Global var
    static ArrayList<Filme> movies = new ArrayList<Filme>();
    static ArrayList<String> moviesIgnoredLines = new ArrayList<String>();
    static ArrayList<String> votesIgnoredLines = new ArrayList<String>();
    static ArrayList<String> peopleIgnoredLines = new ArrayList<String>();
    static ArrayList<String> genresIgnoredLines = new ArrayList<String>();

    public static void lerFicheiros() throws IOException {
        MoviesData moviesReader = Reader.movieReader();
        movies = moviesReader.movies;
        moviesIgnoredLines = moviesReader.ignoredLines;
        votesIgnoredLines = Reader.movieVotesReader(movies);
        peopleIgnoredLines = Reader.peopleReader();
        genresIgnoredLines = Reader.genresReader();
    }

    public static ArrayList<Filme> getFilmes() {
        return movies;
    }

    public static ArrayList<String> getLinhasIgnoradas(String fileName) {
        return switch (fileName) {
            case "deisi_movies.txt" -> moviesIgnoredLines;
            case "deisi_movie_votes.txt" -> votesIgnoredLines;
            case "deisi_people.txt" -> peopleIgnoredLines;
            case "deisi_genres.txt" -> genresIgnoredLines;
            default -> null;
        };
    }

    public static QueryResult perguntar(String pergunta) {
        // Checks if the query is valid
        // Valid -> runs query and returns it
        // Invalid -> prints invalid query message and returns null (??? not sure)

        // Gets query code
        String code = "";
        String data = "";
        if (pergunta.length() > 1) {
            // Divides the query in 'code' and 'data' (which holds the data for the code)
            String[] query = pergunta.split(" ", 2);
            code = query[0];
            data = query[1];
        }

        return switch (code) {
            case "COUNT_MOVIES_ACTOR" -> QueryFunctions.countMoviesActor(data);
            case "GET_MOVIES_ACTOR_YEAR" -> QueryFunctions.getMoviesActorYear(data);
            case "COUNT_MOVIES_WITH_ACTORS" -> QueryFunctions.countMoviesWithActors(data);
            case "COUNT_ACTORS_3_YEARS" -> QueryFunctions.countActors3Years(data);
            case "TOP_MOVIES_WITH_GENDER_BIAS" -> QueryFunctions.topMoviesWithGenderBias(data);
            case "GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR" -> QueryFunctions.getRecentTitlesSameAVGVotesOneSharedActor(data);
            case "GET_TOP_N_YEARS_BEST_AVG_VOTES" -> QueryFunctions.getTopNYearsBestAVGVotes(data);
            case "DISTANCE_BETWEEN_ACTORS" -> QueryFunctions.distanceBetweenActors(data);
            case "GET_TOP_N_MOVIES_RATIO" -> QueryFunctions.getTopNMoviesRatio(data);
            case "TOP_6_DIRECTORS_WITHIN_FAMILY" -> QueryFunctions.top6DirectorsWithinFamily(data);
            case "GET_TOP_ACTOR_YEAR" -> QueryFunctions.getTopActorYear(data);
            case "INSERT_ACTOR" -> QueryFunctions.insertActor(data);
            case "REMOVE_ACTOR" -> QueryFunctions.removeActor(data);
            case "GET_DUPLICATE_LINES_YEAR" -> QueryFunctions.getDuplicateLinesYear(data);
            default -> null;
        };
    }

    public static String getVideoURL() {
        // TODO: Add video here
        return "";
    }

    public static String getCreativeQuery() {
        // TODO: Implement creative query
        return "";
    }

    public static void main(String[] args) throws IOException {
        // Reads input files
        lerFicheiros();

//        DEBUGGING
//        for (Filme movie : movies) {
//            System.out.println(movie.toString());
//        }
//        System.out.println("Total filmes: " + movies.size());
//        System.out.println("Total filmes ignorados: " + moviesIgnoredLines.size());

        // Main program loop
        System.out.println("Bem vindo ao DEISIFLIX");
        // Reads input until user 'QUIT's the program
        Scanner input = new Scanner(System.in);
        String line = input.nextLine();

        while (line != null && !line.equals("QUIT")) {
            // Gets 'QueryResult'
            QueryResult result = perguntar(line);

            if (result == null) {  // Prints invalid query message and asks for new query
                System.out.println("Pergunta desconhecida. Tente novamente.");
            } else {
                // Prints 'QueryResult' value and elapsed time
                System.out.println(result.valor);
                System.out.println("demorou " + result.tempo + " ms");
            }

            // Gets new line
            line = input.nextLine();
        }
    }
}
