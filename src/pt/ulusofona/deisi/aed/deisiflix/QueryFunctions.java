package pt.ulusofona.deisi.aed.deisiflix;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class QueryFunctions {
    // Elapsed time variables
    static long startTime = 0;
    static long endTime = 0;

    // Date File Format
    static DateTimeFormatter dateFileFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /* Query useful classes */
    // Class to store data for 'GET_MOVIES_ACTOR_YEAR'
    static class MovieActorYear {
        String title;
        LocalDate date;

        MovieActorYear(String title, LocalDate date) {
            this.title = title;
            this.date = date;
        }
    }

    // Class to store data for 'TOP_MOVIES_WITH_GENDER_BIAS' query
    static class MovieGenderBias {
        String title;
        int discrepancyPercentage;
        char predominantGender;

        MovieGenderBias(String title, int discrepancyPercentage, char predominantGender) {
            this.title = title;
            this.discrepancyPercentage = discrepancyPercentage;
            this.predominantGender = predominantGender;
        }
    }

    // Class to store data for 'GET_TOP_N_YEARS_BEST_AVG_VOTES' query
    static class AVGVotesByYear {
        int year;
        float avgVotes;

        AVGVotesByYear(int year, float avgVotes) {
            this.year = year;
            this.avgVotes = avgVotes;
        }
    }

    /**
     * 'COUNT_MOVIES_ACTOR' Query.
     * Returns the number of movies an actor has been part of. If the actor does not exist, returns 0.
     * @param data Query arguments
     * @param people HashMap (KEY: name, VALUE: MovieAssociate) that stores all people
     * @return Returns the number of movies an actor has participated in. Returns 0 if actors does not exist.
     */
    public static QueryResult countMoviesActor(String data, HashMap<String, MovieAssociate> people) {
        // TODO (NOTE): Maybe store movies in an HashMap (KEY -> Movie ID, VALUE -> Filme)
        startTime = System.currentTimeMillis();
        String name = data;  // Gets name from data
        int moviesCount = 0;

        // Check if the actor exists
        if (people.containsKey(name)) {
            // Get actor movie count
            moviesCount = people.get(name).associatedMoviesID.size();
        }

        endTime = System.currentTimeMillis();
        String resultValue = "" + moviesCount;
        return new QueryResult(resultValue, (endTime - startTime));
    }

    /**
     * 'GET_MOVIES_ACTOR_YEAR' Query.
     * Returns the movies an actor took part in for a particular year in descending order (by date).
     * @param data Query Arguments
     * @param people HashMap (KEY: name, VALUE: MovieAssociate) that stores all people
     * @param sortedMovies Array with all movies (sorted by ID)
     * @return Returns all the movies the given actor took part in the given year
     */
    public static QueryResult getMoviesActorYear(String data, HashMap<String, MovieAssociate> people, Filme[] sortedMovies) {
        startTime = System.currentTimeMillis();
        String[] queryArguments = data.split(" ");

        // Uses 'StringBuilder' to build actor name from query args
        // Reason: More efficient than 'String' concatenation
        StringBuilder name = new StringBuilder();
        name.append(queryArguments[0]);
        name.append(" ");
        name.append(queryArguments[1]);
        int queryYear = Integer.parseInt(queryArguments[2]);  // Gets year from query arguments

        // Creates 'ArrayList' to store movies the actor participated in the given year
        ArrayList<MovieActorYear> moviesActorYear = new ArrayList<>();
        // ArrayList with all the movies the person has been part of
        ArrayList<Integer> personMovies = people.get(name.toString()).associatedMoviesID;

        for (int i = 0; i < personMovies.size(); i++) {
            // Gets movie position in 'sortedMovies'
            int moviePos = SearchAlgorithms.binarySearchMovieByID(sortedMovies, personMovies.get(i));
            // Gets movie year for the current movie ID being checked
            if (moviePos != -1) {  // Checks if movie exists in 'sortedMovies'
                LocalDate movieDate = LocalDate.parse(sortedMovies[moviePos].dataLancamento, dateFileFormat);
                int movieYear = movieDate.getYear();

                // If year matches, add it to 'moviesActorYear'
                if (movieYear == queryYear) {
                    String title = sortedMovies[moviePos].titulo;
                    moviesActorYear.add(new MovieActorYear(title, movieDate));
                }
            }
        }

        // Sorts 'moviesActorYear' (using SelectionSort) by Date (in descending order)
        SortingAlgorithms.selSortDateByDescendingOrder(moviesActorYear);

        // Builds Output String
        StringBuilder outputString = new StringBuilder();
        DateTimeFormatter outputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < moviesActorYear.size(); i++) {
            MovieActorYear currentMovie = moviesActorYear.get(i);
            if (i == 0) {
                outputString.append(currentMovie.title);
                outputString.append(" (");
                outputString.append(currentMovie.date.format(outputDateFormat));
                outputString.append(")");
            } else {
                outputString.append("\n");
                outputString.append(currentMovie.title);
                outputString.append(" (");
                outputString.append(currentMovie.date.format(outputDateFormat));
                outputString.append(")");
            }
        }

        // TODO: Try to make this more efficient altough its not too bad right now.

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString.toString(), (endTime - startTime));
    }

    /**
     * 'COUNT_MOVIES_WITH_ACTORS' Query.
     * Returns the number of movies in which all the given actors appeared simultaneously.
     * @param data Query Arguments
     * @param people HashMap (KEY: name, VALUE: MovieAssociate) that stores all people
     * @param sortedMovies Array with all movies (sorted by ID)
     * @return Returns the number of movies that contain all the given actors.
     */
    public static QueryResult countMoviesWithActors(String data, HashMap<String, MovieAssociate> people, Filme[] sortedMovies) {
        startTime = System.currentTimeMillis();

        // Gets actor names from query args
        String[] actors = data.split(";", 2);
        // Gets first actor from actors given args and accesses its 'MovieAssociate' entry in 'people'
        MovieAssociate actor1 = people.get(actors[0]);
        // Stores the other actors separated by ';' (semicolons)
        String otherActors = actors[1];

        // Stores output
        int moviesCount = 0;

        // Iterates through 'actor' movies and checks in how many they all took part in
        for (int movieID : actor1.associatedMoviesID) {
            // Gets current movie position in 'sortedMovies'
            int moviePos = SearchAlgorithms.binarySearchMovieByID(sortedMovies, movieID);

            // Checks if movie exists in 'sortedMovies' before accessing it
            if (moviePos != -1) {
                ArrayList<Pessoa> actorsList = sortedMovies[moviePos].atores;
                // Checks if all actors participated in the movie currently being checked
                if (AuxiliaryQueryFunctions.containsActors(actorsList, otherActors)) {
                    moviesCount++;
                }
            }
        }
        String outputString = String.valueOf(moviesCount);

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString, (endTime - startTime));
    }

    /**
     * 'COUNT_ACTORS_3_YEARS' Query.
     * Returns the number of unique actors that took part in movies in the given years.
     * @param data Query Arguments
     * @param movieIDsByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @param sortedMovies Array with all movies (sorted by ID)
     * @return Returns the unique actors that participated in movies in the given years
     */
    public static QueryResult countActors3Years(String data, HashMap<Integer, ArrayList<Integer>> movieIDsByYear, Filme[] sortedMovies) {
        startTime = System.currentTimeMillis();
        String[] queryArgs = data.split(" ");  // Gets years from query data

        // Creates an Integer array with query args
        int[] queryYears = new int[3];
        queryYears[0] = Integer.parseInt(queryArgs[0]);
        queryYears[1] = Integer.parseInt(queryArgs[1]);
        queryYears[2] = Integer.parseInt(queryArgs[2]);

        // 'ArrayList' to store unique actors' names
        ArrayList<String> uniqueActors = new ArrayList<>();

        for (int queryYear : queryYears) {
            ArrayList<Integer> movies = movieIDsByYear.get(queryYear);
            for (int movieID : movies) {
                // Gets unique actors for each movie and stores them in 'uniqueActors'
                AuxiliaryQueryFunctions.getUniqueMovieActors(movieID, uniqueActors, sortedMovies);
            }
        }
        String outputString = String.valueOf(uniqueActors.size());

        // TODO: Improve this by a lot LULz

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString, (endTime - startTime));
    }

    /**
     * 'TOP_MOVIES_WITH_GENDER_BIAS' Query.
     * Returns the N (given number) movies with the greatest gender percentual discrepancy in the given years.
     * @param data Query Arguments
     * @param moviesByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @param sortedMovies Array with all movies (sorted by ID)
     * @return Returns the number of movies with the greatest gender percentual discrepancy in the given year
     */
    public static QueryResult topMoviesWithGenderBias(
            String data,
            HashMap<Integer, ArrayList<Integer>> moviesByYear,
            Filme[] sortedMovies
    ) {
        startTime = System.currentTimeMillis();
        // Gets query args
        String[] queryArgs = data.split(" ");
        // Gets number of movies to output
        int moviesOutNum = Integer.parseInt(queryArgs[0]);
        // Gets year input
        int year = Integer.parseInt(queryArgs[1]);

        ArrayList<MovieGenderBias> moviesGenderBias = new ArrayList<>();

        // Calculates Gender Percentual Discrepancy for all movies in 'year' and adds them to 'moviesGenderBias'
        AuxiliaryQueryFunctions.calculateGenderPercentualDiscrepancy(year, moviesByYear, sortedMovies, moviesGenderBias);

        // Sorts 'moviesGenderBias' by Gender Percentual Discrepancy (descending)
        SortingAlgorithms.selSortGenderBiasDescending(moviesGenderBias);

        StringBuilder outputString = new StringBuilder();

        // Builds output string
        for (int i = 0; i < moviesOutNum; i++) {
            MovieGenderBias movie = moviesGenderBias.get(i);

            // Dont add '\n' to the last line
            if (i == moviesOutNum - 1) {
                outputString.append(movie.title);
                outputString.append(':');
                outputString.append(movie.predominantGender);
                outputString.append(':');
                outputString.append(movie.discrepancyPercentage);
            } else {
                outputString.append(movie.title);
                outputString.append(':');
                outputString.append(movie.predominantGender);
                outputString.append(':');
                outputString.append(movie.discrepancyPercentage);
                outputString.append('\n');
            }
        }

        // TODO: Make this more efficient. Still has potential to improve.

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString.toString(), (endTime - startTime));
    }

    public static QueryResult getRecentTitlesSameAVGVotesOneSharedActor(String data) {
        startTime = System.currentTimeMillis();
        // TODO
        endTime = System.currentTimeMillis();
        return new QueryResult();
    }

    /**
     * 'GET_TOP_N_YEARS_BEST_AVG_VOTES' Query.
     * Returns the N (given number) of years with the best movie average votes between all movies.
     * @param data Query Arguments
     * @param moviesByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @param sortedMovies Array with all movies (sorted by ID)
     * @return Returns the years with the best average votes
     */
    public static QueryResult getTopNYearsBestAVGVotes(String data, HashMap<Integer, ArrayList<Integer>> moviesByYear, Filme[] sortedMovies) {
        startTime = System.currentTimeMillis();
        // Gets number of movies to output
        int moviesOutputNum = Integer.parseInt(data);

        ArrayList<AVGVotesByYear> votesByYear = new ArrayList<>();

        // Adds each year votes average to 'votesByYear'
        for (Integer movie : moviesByYear.keySet()) {
            float yearVotesAverage = AuxiliaryQueryFunctions.calculateYearVotesAverage(moviesByYear.get(movie), sortedMovies);
            votesByYear.add(new AVGVotesByYear(movie, yearVotesAverage));
        }

        // Sorts 'votesByYear' by average votes (by ascending order)
        SortingAlgorithms.quickSortByAVGVotes(votesByYear);

        StringBuilder outputString = new StringBuilder();
        // Builds output string: starts at the last 'votesByYear' index because it is sorted in ascending order
        for (int pos = votesByYear.size() - 1, i = 0; i < moviesOutputNum; pos--, i++) {
            // Gets current 'AVGVotesByYear' object
            AVGVotesByYear current = votesByYear.get(pos);

            // Appends data to 'outputString'
            outputString.append(current.year);
            outputString.append(':');
            outputString.append(Math.round(current.avgVotes * 100) / 100.0);

            // Does not put '\n' in the last year
            if (i != moviesOutputNum - 1) {
                outputString.append('\n');
            }
        }

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString.toString(), (endTime - startTime));
    }

    public static QueryResult distanceBetweenActors(String data) {
        startTime = System.currentTimeMillis();
        // TODO
        endTime = System.currentTimeMillis();
        return new QueryResult();
    }

    public static QueryResult getTopNMoviesRatio(String data) {
        startTime = System.currentTimeMillis();
        // TODO
        endTime = System.currentTimeMillis();
        return new QueryResult();
    }

    public static QueryResult top6DirectorsWithinFamily(String data) {
        startTime = System.currentTimeMillis();
        // TODO
        endTime = System.currentTimeMillis();
        return new QueryResult();
    }

    public static QueryResult getTopActorYear(String data) {
        startTime = System.currentTimeMillis();
        // TODO
        endTime = System.currentTimeMillis();
        return new QueryResult();
    }

    public static QueryResult insertActor(String data) {
        startTime = System.currentTimeMillis();
        // TODO
        endTime = System.currentTimeMillis();
        return new QueryResult();
    }

    public static QueryResult removeActor(String data) {
        startTime = System.currentTimeMillis();
        // TODO
        endTime = System.currentTimeMillis();
        return new QueryResult();
    }

    public static QueryResult getDuplicateLinesYear(String data) {
        startTime = System.currentTimeMillis();
        // TODO
        endTime = System.currentTimeMillis();
        return new QueryResult();
    }
}
