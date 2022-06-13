package pt.ulusofona.deisi.aed.deisiflix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    // Tells the program which files to use in the Readers (DP files or Local files)
    static boolean DP = true;

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

    /**
     * Reads all input files with all the movies' data and stores that data in
     * appropriate data structures.
     * @throws IOException
     */
    public static void lerFicheiros() throws IOException {
        // Sets Reader files' variables based on DP submission or Local environment
        if (!DP) {
            moviesFile = largeMovies;
            votesFile = largeVotes;
            peopleFile = largePeople;
            genresFile = largeGenres;
        }

        // Executes 'movieReader' which reads all movies' data from 'deisi_movies.txt' file
        // and stores it in appropriate data structures
        MoviesData moviesReader = Reader.movieReader(moviesFile);
        moviesFileOrder = moviesReader.moviesFileOrder;
        moviesDict = moviesReader.moviesDict;
        movieIDsByYear = moviesReader.movieIDsByYear;
        moviesIgnoredLines = moviesReader.ignoredLines;

        // Executes 'movieVotesReader' which reads all movie votes' data from 'deisi_movie_votes.txt' file
        // and stores it in appropriate data structures
        votesIgnoredLines = Reader.movieVotesReader(votesFile, moviesDict);

        // Executes 'peopleReader' which reads all movie people data from 'deisi_people.txt' file
        // and stores it in appropriate data structures
        PeopleData peopleReader = Reader.peopleReader(peopleFile, moviesDict);
        moviesPeople = peopleReader.moviesPeople;
        actorsByID = peopleReader.actorsByID;
        peopleDuplicateLinesYear = peopleReader.duplicateLinesYear;
        peopleIgnoredLines = peopleReader.ignoredLines;

        // Executes 'genresReader' which reads all movie genres' data from 'deisi_genres.txt' file
        // and stores it in appropriate data structures
        genresIgnoredLines = Reader.genresReader(genresFile, moviesDict);
    }

    /**
     * Reads all test input files with all the movies' test data and stores that data in
     * appropriate data structures.
     * @throws IOException
     */
    public static void lerFicheirosTestes() throws IOException {
        // Executes 'movieReader' which reads all movies' data from 'test_movies.txt' file
        // and stores it in appropriate data structures
        MoviesData moviesReader = Reader.movieReader(testMoviesFile);
        moviesFileOrder = moviesReader.moviesFileOrder;
        moviesDict = moviesReader.moviesDict;
        movieIDsByYear = moviesReader.movieIDsByYear;
        moviesIgnoredLines = moviesReader.ignoredLines;

        // Executes 'movieVotesReader' which reads all movie votes' data from 'test_movie_votes.txt' file
        // and stores it in appropriate data structures
        votesIgnoredLines = Reader.movieVotesReader(testVotesFile, moviesDict);

        // Executes 'peopleReader' which reads all movie people data from 'test_people.txt' file
        // and stores it in appropriate data structures
        PeopleData peopleReader = Reader.peopleReader(testPeopleFile, moviesDict);
        moviesPeople = peopleReader.moviesPeople;
        actorsByID = peopleReader.actorsByID;
        peopleDuplicateLinesYear = peopleReader.duplicateLinesYear;
        peopleIgnoredLines = peopleReader.ignoredLines;

        // Executes 'genresReader' which reads all movie genres' data from 'test_genres.txt' file
        // and stores it in appropriate data structures
        genresIgnoredLines = Reader.genresReader(testGenresFile, moviesDict);
    }

    /**
     * Returns an ArrayList of type 'Filme' with all the movies which are present in
     * 'deisi_movies.txt' file preserving the file order.
     * @return Returns all movies in movies file with preserved order
     */
    public static ArrayList<Filme> getFilmes() {
        return moviesFileOrder;
    }

    /**
     * Returns the ignored lines (lines that don't match the number of expected components)
     * for the given file.
     * @param fileName The name of the file to which the function must be executed
     * @return Returns the ignored lines for the given file name
     */
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

    /**
     * Executes and returns the value of the inserted query..
     * If the query is valid, it returns the query output.
     * If the query is not valid, it returns null.
     * @param pergunta The given query to be executed
     * @return Returns the value of the executed query
     */
    public static QueryResult perguntar(String pergunta) {
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

    /**
     * Returns a video explaining one the the non mandatory queries
     * as well as the creative query.
     * @return Returns the video link
     */
    public static String getVideoURL() {
        return "https://youtu.be/Ur6ENIPnFXY";
    }

    /**
     * 'TOP_N_MOST_EXPENSIVE_MOVIES_YEAR' Query.
     * Returns the top 10 movies with the largest budget for the given year.
     * Input Format: "TOP_10_MOST_EXPENSIVE_MOVIES_YEAR <Year>"
     * Output Format: "<MovieTitle> - Budget: $<MovieBudget>" (separated by '\n', newline character)
     * @return Returns the Creative Query
     */
    public static String getCreativeQuery() {
        return "TOP_10_MOST_EXPENSIVE_MOVIES_YEAR";
    }

    public static void main(String[] args) throws IOException {
        lerFicheiros();  // Reads input files

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
