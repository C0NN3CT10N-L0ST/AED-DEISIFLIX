package pt.ulusofona.deisi.aed.deisiflix;

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

    // Creates a class to store movie 'Title' and 'Date'
    static class MovieActorYear {
        String title;
        LocalDate date;

        MovieActorYear(String title, LocalDate date) {
            this.title = title;
            this.date = date;
        }
    }

    /*
        Query: 'COUNT_MOVIES_ACTOR'
        Returns the number of movies an actor has been part of.
        If the actor does not exist, it must return 0.
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
        String resultValue = "" + moviesCount;  // Result String
        return new QueryResult(resultValue, (endTime - startTime));
    }

    public static QueryResult getMoviesActorYear(String data, HashMap<String, MovieAssociate> people, Filme[] sortedMovies) {
        startTime = System.currentTimeMillis();
        String[] queryArguments = data.split(" ");

        // Uses 'StringBuilder' to build actor name from query args
        // Reason: More efficient than 'String' concatenation
        StringBuilder name = new StringBuilder();
        name.append(queryArguments[0]);
        name.append(" ");
        name.append(queryArguments[1]);
        int queryYear = Integer.parseInt(queryArguments[2]);  // Gets year from query data

        // Create 'ArrayList' to store movies the actor participated in the given year
        ArrayList<MovieActorYear> moviesActorYear = new ArrayList<>();
        // Person Movies ArrayList
        ArrayList<Integer> personMovies = people.get(name.toString()).associatedMoviesID;

        for (int i = 0; i < personMovies.size(); i++) {
            // Gets movie position in 'sortedMovies'
            int moviePos = SearchAlgorithms.binarySearchMovieByID(sortedMovies, personMovies.get(i));
            // Gets movie year for the current ID
            LocalDate movieDate = LocalDate.parse(sortedMovies[moviePos].dataLancamento, dateFileFormat);
            int movieYear = movieDate.getYear();

            // If year matches, add it to 'moviesActorYear'
            if (movieYear == queryYear) {
                String title = sortedMovies[moviePos].titulo;
                moviesActorYear.add(new MovieActorYear(title, movieDate));
            }
        }

        // Sorts 'moviesActorYear' (using SelectionSort) by Date (descending)
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

    public static QueryResult countMoviesWithActors(String data, HashMap<String, MovieAssociate> people, Filme[] sortedMovies) {
        startTime = System.currentTimeMillis();

        // Gets actor names from query args
        String[] actors = data.split(";", 2);
        // Gets first actor from actors given args and accesses its 'MovieAssociate' entry in the HashMap
        MovieAssociate actor1 = people.get(actors[0]);
        // Stores the other actors separated by ';' (semicolons)
        String otherActors = actors[1];

        int moviesCount = 0;

        // Iterates through 'actor' movies and checks in how many they all took part in
        for (int movieID : actor1.associatedMoviesID) {
            // Gets current movie position in 'sortedMovies'
            int moviePos = SearchAlgorithms.binarySearchMovieByID(sortedMovies, movieID);
            // TODO: finish this. Waiting on 'peopleReader' to be able to add people to 'sortedMovies' properly

            // Checks if movie exists in 'sortedMovies' before accessing it
            if (moviePos != -1) {
                ArrayList<Pessoa> actorsList = sortedMovies[moviePos].atores;
                if (AuxiliaryQueryFunctions.containsActors(actorsList, otherActors)) {
                    moviesCount++;
                }
            }
        }
        String outputString = String.valueOf(moviesCount);

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString, (endTime - startTime));
    }

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

        for (int i = 0; i < queryYears.length; i++) {
            for (Integer movieID : movieIDsByYear.keySet()) {
                AuxiliaryQueryFunctions.getUniqueMovieActors(movieID, uniqueActors, sortedMovies);
            }
        }
        String outputString = String.valueOf(uniqueActors.size());

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString, (endTime - startTime));
    }

    public static QueryResult topMoviesWithGenderBias(String data) {
        startTime = System.currentTimeMillis();
        // TODO
        endTime = System.currentTimeMillis();
        return new QueryResult();
    }

    public static QueryResult getRecentTitlesSameAVGVotesOneSharedActor(String data) {
        startTime = System.currentTimeMillis();
        // TODO
        endTime = System.currentTimeMillis();
        return new QueryResult();
    }

    public static QueryResult getTopNYearsBestAVGVotes(String data) {
        startTime = System.currentTimeMillis();
        // TODO
        endTime = System.currentTimeMillis();
        return new QueryResult();
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
