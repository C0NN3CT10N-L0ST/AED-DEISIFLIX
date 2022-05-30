package pt.ulusofona.deisi.aed.deisiflix;

import java.util.HashMap;

public class QueryFunctions {
    // Elapsed time variables
    static long startTime = 0;
    static long endTime = 0;

    /*
        Returns the number of movies an actor has been part of.
        If the actor does not exist, it must return 0.
    */
    public static QueryResult countMoviesActor(String data, HashMap<String, MovieAssociate> people) {
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

    public static QueryResult getMoviesActorYear(String data) {
        startTime = System.currentTimeMillis();
        // TODO
        endTime = System.currentTimeMillis();
        return new QueryResult();
    }

    public static QueryResult countMoviesWithActors(String data) {
        startTime = System.currentTimeMillis();
        // TODO
        endTime = System.currentTimeMillis();
        return new QueryResult();
    }

    public static QueryResult countActors3Years(String data) {
        startTime = System.currentTimeMillis();
        // TODO
        endTime = System.currentTimeMillis();
        return new QueryResult();
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
