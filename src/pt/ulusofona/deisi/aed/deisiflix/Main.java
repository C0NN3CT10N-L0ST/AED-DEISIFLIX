package pt.ulusofona.deisi.aed.deisiflix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    /* Global variables */
    static ArrayList<Filme> moviesFileOrder;
    static HashMap<Integer, Filme> moviesDict;
    static HashMap<Integer, ArrayList<Integer>> movieIDsByYear;
    static ArrayList<String> moviesIgnoredLines;
    static ArrayList<String> votesIgnoredLines;
    static HashMap<String, ArrayList<MovieAssociate>> moviesPeople;
    static HashMap<Integer, String> actorsByID;
    static HashMap<Integer, ArrayList<String>> peopleDuplicateLinesYear;
    static ArrayList<String> peopleIgnoredLines;
    static ArrayList<String> genresIgnoredLines;

    public static void lerFicheiros() throws IOException {
        MoviesData moviesReader = Reader.movieReader();
        moviesFileOrder = moviesReader.moviesFileOrder;
        moviesDict = moviesReader.moviesDict;
        movieIDsByYear = moviesReader.movieIDsByYear;
        moviesIgnoredLines = moviesReader.ignoredLines;
        votesIgnoredLines = Reader.movieVotesReader(moviesDict);
        PeopleData peopleReader = Reader.peopleReader(moviesDict);
        moviesPeople = peopleReader.moviesPeople;
        actorsByID = peopleReader.actorsByID;
        peopleDuplicateLinesYear = peopleReader.duplicateLinesYear;
        peopleIgnoredLines = peopleReader.ignoredLines;
        genresIgnoredLines = Reader.genresReader(moviesDict);

        // TODO: document this
    }

    public static ArrayList<Filme> getFilmes() {
        return moviesFileOrder;
    }

    public static ArrayList<String> getLinhasIgnoradas(String fileName) {
        switch (fileName) {
            case "deisi_movies.txt":
                return moviesIgnoredLines;
            case "deisi_movie_votes.txt":
                return votesIgnoredLines;
            case "deisi_people.txt":
                return peopleIgnoredLines;
            case "deisi_genres.txt":
                return genresIgnoredLines;
            default:
                return null;
        }
    }

    public static QueryResult perguntar(String pergunta) {
        // Checks if the query is valid
        // Valid -> runs query and returns it
        // Invalid -> returns null (??? not sure)

        // Gets query code
        String code = "";
        String data = "";
        String[] query = pergunta.split(" ", 2);

        if (query.length > 1) {
            // Divides the query in 'code' and 'data' (which holds the data for the code)
            code = query[0];
            data = query[1];
        }

        switch (code) {
            case "COUNT_MOVIES_ACTOR":
                return QueryFunctions.countMoviesActor(data, moviesPeople);
            case "GET_MOVIES_ACTOR_YEAR":
                return QueryFunctions.getMoviesActorYear(data, moviesPeople, moviesDict);
            case "COUNT_MOVIES_WITH_ACTORS":
                return QueryFunctions.countMoviesWithActors(data, moviesPeople, moviesDict);
            case "COUNT_ACTORS_3_YEARS":
                return QueryFunctions.countActors3Years(data, movieIDsByYear, moviesDict);
            case "TOP_MOVIES_WITH_GENDER_BIAS":
                return QueryFunctions.topMoviesWithGenderBias(data, movieIDsByYear, moviesDict);
            case "GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR":
                return QueryFunctions.getRecentTitlesSameAVGVotesOneSharedActor(data, moviesDict, movieIDsByYear);
            case "GET_TOP_N_YEARS_BEST_AVG_VOTES":
                return QueryFunctions.getTopNYearsBestAVGVotes(data, movieIDsByYear, moviesDict);
            case "DISTANCE_BETWEEN_ACTORS":
                return QueryFunctions.distanceBetweenActors(data, moviesPeople, moviesDict);
            case "GET_TOP_N_MOVIES_RATIO":
                return QueryFunctions.getTopNMoviesRatio(data, movieIDsByYear, moviesDict);
            case "TOP_6_DIRECTORS_WITHIN_FAMILY":
                return QueryFunctions.top6DirectorsWithinFamily(data);
            case "GET_TOP_ACTOR_YEAR":
                return QueryFunctions.getTopActorYear(data, movieIDsByYear, moviesDict);
            case "INSERT_ACTOR":
                return QueryFunctions.insertActor(data, moviesPeople, moviesDict, actorsByID);
            case "REMOVE_ACTOR":
                return QueryFunctions.removeActor(data, moviesPeople, moviesDict, actorsByID);
            case "GET_DUPLICATE_LINES_YEAR":
                return QueryFunctions.getDuplicateLinesYear(data, peopleDuplicateLinesYear);
            default:
                return null;
        }
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
        // long readTimerStart = System.currentTimeMillis();
        lerFicheiros();  // Reads input files
        // long readTimerEnd = System.currentTimeMillis();
        // System.out.println("Tempo de leitura dos ficheiros: " + (readTimerEnd - readTimerStart));

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
                System.out.println("(demorou " + result.tempo + " ms)");
            }

            // Gets new line
            line = input.nextLine();
        }
    }
}
