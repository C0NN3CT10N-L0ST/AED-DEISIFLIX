package pt.ulusofona.deisi.aed.deisiflix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    // Tells the program which files to use in the Readers (DP files or Local files)
    static boolean DP = false;

    // Reader files' paths
    static String moviesFile = "deisi_movies.txt";
    static String votesFile = "deisi_movie_votes.txt";
    static String peopleFile = "deisi_people.txt";
    static String genresFile = "deisi_genres.txt";

    // Local database files' paths
    static String largeMovies = "local-test-files/deisi_movies_large.txt";
    static String largeVotes = "local-test-files/deisi_movie_votes_large.txt";
    static String largePeople = "local-test-files/deisi_people_large.txt";
    static String largeGenres = "local-test-files/deisi_genres_large.txt";

    // Test files' paths
    static String testMoviesFile = "test-files/test_movies.txt";
    static String testVotesFile = "test-files/test_movie_votes.txt";
    static String testPeopleFile = "test-files/test_people.txt";
    static String testGenresFile = "test-files/test_genres.txt";

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

    /* Global variables (Tests) */
    static ArrayList<Filme> testMoviesFileOrder;
    static HashMap<Integer, Filme> testMoviesDict;
    static HashMap<Integer, ArrayList<Integer>> testMovieIDsByYear;
    static ArrayList<String> testMoviesIgnoredLines;
    static ArrayList<String> testVotesIgnoredLines;
    static HashMap<String, ArrayList<MovieAssociate>> testMoviesPeople;
    static HashMap<Integer, String> testActorsByID;
    static HashMap<Integer, ArrayList<String>> testPeopleDuplicateLinesYear;
    static ArrayList<String> testPeopleIgnoredLines;
    static ArrayList<String> testGenresIgnoredLines;

    public static void lerFicheiros() throws IOException {
        // Sets Reader files' variables based on DP submission or Local environment
        if (!DP) {
            moviesFile = largeMovies;
            votesFile = largeVotes;
            peopleFile = largePeople;
            genresFile = largeGenres;
        }

        MoviesData moviesReader = Reader.movieReader(moviesFile);
        moviesFileOrder = moviesReader.moviesFileOrder;
        moviesDict = moviesReader.moviesDict;
        movieIDsByYear = moviesReader.movieIDsByYear;
        moviesIgnoredLines = moviesReader.ignoredLines;
        votesIgnoredLines = Reader.movieVotesReader(votesFile, moviesDict);
        PeopleData peopleReader = Reader.peopleReader(peopleFile, moviesDict);
        moviesPeople = peopleReader.moviesPeople;
        actorsByID = peopleReader.actorsByID;
        peopleDuplicateLinesYear = peopleReader.duplicateLinesYear;
        peopleIgnoredLines = peopleReader.ignoredLines;
        genresIgnoredLines = Reader.genresReader(genresFile, moviesDict);

        // TODO: document this
    }

    public static void lerFicheirosTestes() throws IOException {
        MoviesData moviesReader = Reader.movieReader(testMoviesFile);
        moviesFileOrder = moviesReader.moviesFileOrder;
        moviesDict = moviesReader.moviesDict;
        movieIDsByYear = moviesReader.movieIDsByYear;
        moviesIgnoredLines = moviesReader.ignoredLines;
        votesIgnoredLines = Reader.movieVotesReader(testVotesFile, moviesDict);
        PeopleData peopleReader = Reader.peopleReader(testPeopleFile, moviesDict);
        moviesPeople = peopleReader.moviesPeople;
        actorsByID = peopleReader.actorsByID;
        peopleDuplicateLinesYear = peopleReader.duplicateLinesYear;
        peopleIgnoredLines = peopleReader.ignoredLines;
        genresIgnoredLines = Reader.genresReader(testGenresFile, moviesDict);


        // TODO: Document this
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
                return QueryFunctions.top6DirectorsWithinFamily(data, movieIDsByYear, moviesDict);
            case "GET_TOP_ACTOR_YEAR":
                return QueryFunctions.getTopActorYear(data, movieIDsByYear, moviesDict);
            case "INSERT_ACTOR":
                return QueryFunctions.insertActor(data, moviesPeople, moviesDict, actorsByID);
            case "REMOVE_ACTOR":
                return QueryFunctions.removeActor(data, moviesPeople, moviesDict, actorsByID);
            case "GET_DUPLICATE_LINES_YEAR":
                return QueryFunctions.getDuplicateLinesYear(data, peopleDuplicateLinesYear);
            case "TOP_10_MOST_EXPENSIVE_MOVIES_YEAR":
                return QueryFunctions.topNMostExpensiveMoviesYear(data, movieIDsByYear, moviesDict);
            default:
                return null;
        }
    }

    public static String getVideoURL() {
        // TODO: Add video here
        return "";
    }

    public static String getCreativeQuery() {
        /*
            'TOP_N_MOST_EXPENSIVE_MOVIES_YEAR' Query.
            Returns the top 10 movies with the largest budget for the given year.
            Input Format: "TOP_10_MOST_EXPENSIVE_MOVIES_YEAR <Year>"
            Output Format: "<MovieTitle> - Budget: $<MovieBudget>" (separated by '\n', newline character)
         */
        return "TOP_10_MOST_EXPENSIVE_MOVIES_YEAR";
    }

    public static void main(String[] args) throws IOException {
        // long readTimerStart = System.currentTimeMillis();
        lerFicheiros();  // Reads input files
        // long readTimerEnd = System.currentTimeMillis();
        // System.out.println("Tempo de leitura dos ficheiros: " + (readTimerEnd - readTimerStart));

        // DEBUG
        //System.out.println(moviesPeople.get("Kunihiko Yuyama").get(0).associatedMoviesID);
        /*System.out.println("Duplicate lines in " + 2000);
        for (String line : peopleDuplicateLinesYear.get(2000)) {
            System.out.println(line);
        } */

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
