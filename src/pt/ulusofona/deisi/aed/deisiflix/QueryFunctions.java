package pt.ulusofona.deisi.aed.deisiflix;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    // Class to store data for 'GET_TOP_N_MOVIES_RATIO' query
    static class MovieRatio {
        String title;
        double ratio;

        MovieRatio(String title, double ratio) {
            this.title = title;
            this.ratio = ratio;
        }
    }

    // Class to store data for 'TOP_6_DIRECTORS_WITHIN_FAMILY' query
    static class DirectorsFamily {
        String name;
        int familyDirections;

        DirectorsFamily(String name, int familyDirections) {
            this.name = name;
            this.familyDirections = familyDirections;
        }
    }

    // Class to store data for 'TOP_N_MOST_EXPENSIVE_MOVIES_YEAR' query
    static class MovieBudget {
        String title;
        int budget;

        MovieBudget(String title, int budget) {
            this.title = title;
            this.budget = budget;
        }
    }

    /* ----- QUERIES ----- */

    /**
     * 'COUNT_MOVIES_ACTOR' Query.
     * Returns the number of movies an actor has been part of. If the actor does not exist, returns 0.
     * @param data Query arguments
     * @param people HashMap (KEY: name, VALUE: ArrayList with MovieAssociate's) that stores all people
     * @return Returns the number of movies an actor has participated in. Returns 0 if actors does not exist.
     */
    public static QueryResult countMoviesActor(String data, HashMap<String, ArrayList<MovieAssociate>> people) {
        startTime = System.currentTimeMillis();
        String name = data;  // Gets name from data
        int moviesCount = 0;

        // Check if the actor exists
        if (people.containsKey(name)) {
            // Get actor movie count
            for (MovieAssociate movieAssociate : people.get(name)) {
                if (movieAssociate.type.equals("ACTOR")) {
                    moviesCount += movieAssociate.associatedMoviesID.size();
                }
            }
        }

        endTime = System.currentTimeMillis();
        String resultValue = "" + moviesCount;
        return new QueryResult(resultValue, (endTime - startTime));
    }

    /**
     * 'GET_MOVIES_ACTOR_YEAR' Query.
     * Returns the movies an actor took part in for a particular year in descending order (by date).
     * @param data Query Arguments
     * @param people HashMap (KEY: name, VALUE: ArrayList with MovieAssociate's) that stores all people
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns all the movies the given actor took part in the given year
     */
    public static QueryResult getMoviesActorYear(
            String data, HashMap<String, ArrayList<MovieAssociate>> people, HashMap<Integer, Filme> moviesDict
    ) {
        startTime = System.currentTimeMillis();
        String[] queryArguments = data.split(" ");

        // Uses 'StringBuilder' to build actor name from query args
        StringBuilder name = new StringBuilder();
        name.append(queryArguments[0]);
        name.append(" ");
        name.append(queryArguments[1]);

        // Gets year from query arguments
        int queryYear = Integer.parseInt(queryArguments[2]);

        StringBuilder outputString = new StringBuilder();

        // Checks if the given name exists
        if (people.get(name.toString()) != null) {
            // Stores movies the actor participated in the given year
            ArrayList<MovieActorYear> moviesActorYear = new ArrayList<>();

            // Gets current actor
            ArrayList<MovieAssociate> actors = people.get(name.toString());

            for (MovieAssociate actor : actors) {
                // ArrayList with all the movies the person has been part of
                ArrayList<Integer> personMovies = actor.associatedMoviesID;

                AuxiliaryQueryFunctions.getMoviesFromYear(
                        queryYear, dateFileFormat, personMovies, moviesDict, moviesActorYear);
            }

            // Sorts 'moviesActorYear' (using SelectionSort) by Date (in descending order)
            SortingAlgorithms.selSortDateByDescendingOrder(moviesActorYear);

            // Sets Date format to be used in the output
            DateTimeFormatter outputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Builds Output String
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
        }

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString.toString(), (endTime - startTime));
    }

    /**
     * 'COUNT_MOVIES_WITH_ACTORS' Query.
     * Returns the number of movies in which all the given actors appeared simultaneously.
     * @param data Query Arguments
     * @param people HashMap (KEY: name, VALUE: ArrayList with MovieAssociate's) that stores all people
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns the number of movies that contain all the given actors.
     */
    public static QueryResult countMoviesWithActors(
            String data, HashMap<String, ArrayList<MovieAssociate>> people, HashMap<Integer, Filme> moviesDict
    ) {
        startTime = System.currentTimeMillis();

        // Gets actor names from query args
        String[] actors = data.split(";", 2);
        // Gets first actor from actors given args and accesses its 'MovieAssociate' entry in 'people'
        MovieAssociate actor1 = people.get(actors[0]).get(0);
        // Stores the other actors separated by ';' (semicolons)
        String otherActors = actors[1];

        // Stores output
        int moviesCount = 0;

        // Iterates through 'actor' movies and checks in how many they all took part in
        for (int movieID : actor1.associatedMoviesID) {
            // Gets current movie being checked
            Filme movie = moviesDict.get(movieID);

            if (movie != null) {
                ArrayList<Pessoa> actorsList = movie.atores;
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
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns the unique actors that participated in movies in the given years
     */
    public static QueryResult countActors3Years(
            String data, HashMap<Integer, ArrayList<Integer>> movieIDsByYear, HashMap<Integer, Filme> moviesDict
    ) {
        startTime = System.currentTimeMillis();
        String[] queryArgs = data.split(" ");  // Gets years from query data

        // Creates an Integer array with query args
        int[] queryYears = new int[3];
        queryYears[0] = Integer.parseInt(queryArgs[0]);
        queryYears[1] = Integer.parseInt(queryArgs[1]);
        queryYears[2] = Integer.parseInt(queryArgs[2]);

        int year1 = queryYears[0];
        int year2 = queryYears[1];
        int year3 = queryYears[2];

        // 'HashSet' to store unique actors' names
        HashSet<String> uniqueActorsYear1 = new HashSet<>();
        HashSet<String> uniqueActorsYear2 = new HashSet<>();
        HashSet<String> uniqueActorsYear3 = new HashSet<>();

        // Adds every actor from each movie in the given year to its correspondent 'HashSet'
        AuxiliaryQueryFunctions.getUniqueMovieActors(year1, movieIDsByYear, uniqueActorsYear1, moviesDict);
        AuxiliaryQueryFunctions.getUniqueMovieActors(year2, movieIDsByYear, uniqueActorsYear2, moviesDict);
        AuxiliaryQueryFunctions.getUniqueMovieActors(year3, movieIDsByYear, uniqueActorsYear3, moviesDict);

        int uniqueActorsCount = 0;

        // Counts unique actors from given years
        for (String actor : uniqueActorsYear1) {
            // Check if 'uniqueActorsYear2' and 'uniqueActorsYear3' also contain the actor, if so, increment counter
            if (uniqueActorsYear2.contains(actor) && uniqueActorsYear3.contains(actor)) {
                uniqueActorsCount++;
            }
        }

        String outputString = String.valueOf(uniqueActorsCount);

        // TODO: Still has margin for improvement

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString, (endTime - startTime));
    }

    /**
     * 'TOP_MOVIES_WITH_GENDER_BIAS' Query.
     * Returns the N (given number) movies with the greatest gender percentual discrepancy in the given years.
     * @param data Query Arguments
     * @param moviesByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns the number of movies with the greatest gender percentual discrepancy in the given year
     */
    public static QueryResult topMoviesWithGenderBias(
            String data, HashMap<Integer, ArrayList<Integer>> moviesByYear, HashMap<Integer, Filme> moviesDict
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
        AuxiliaryQueryFunctions.calculateGenderPercentualDiscrepancy(year, moviesByYear, moviesDict, moviesGenderBias);

        // Sorts 'moviesGenderBias' by Gender Percentual Discrepancy (descending)
        SortingAlgorithms.selSortGenderBiasDescending(moviesGenderBias);

        StringBuilder outputString = new StringBuilder();

        // Builds output string
        for (int i = 0; i < moviesOutNum; i++) {
            MovieGenderBias movie = moviesGenderBias.get(i);

            // Dont add '\n' to the last line
            outputString.append(movie.title);
            outputString.append(':');
            outputString.append(movie.predominantGender);
            outputString.append(':');
            outputString.append(movie.discrepancyPercentage);

            if (i != moviesOutNum - 1) {
                outputString.append('\n');
            }
        }

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString.toString(), (endTime - startTime));
    }

    /**
     * 'GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR' Query.
     * Returns the movie titles that have the same average as the given movie,
     * made after it and that have exactly ONE shared actor between them.
     * @param data Query Arguments
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @param moviesByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @return Returns the movies with same average votes as the given one, made after it, and with a shared actor
     */
    public static QueryResult getRecentTitlesSameAVGVotesOneSharedActor(
            String data, HashMap<Integer, Filme> moviesDict, HashMap<Integer, ArrayList<Integer>> moviesByYear
    ) {
        startTime = System.currentTimeMillis();
        // TODO: Simplify this. The code looks like SHIT xP
        // Gets query args
        int id = Integer.parseInt(data);

        // Get movie with query id
        Filme movie = moviesDict.get(id);
        double avgVotes = movie.mediaVotos;
        LocalDate date = LocalDate.parse(movie.dataLancamento, dateFileFormat);

        // Creates an Array with all the IDs from the given movie actors
        ArrayList<Integer> actorIDs = new ArrayList<>();

        // Adds each actor ID from given movie to 'actorIDs'
        for (Pessoa actor : movie.atores) {
            actorIDs.add(actor.id);
        }

        // Stores valid movie titles
        ArrayList<String> validMovieTitles = new ArrayList<>();

        // Checks all the movies in the same year or older
        for (int year = date.getYear(); year < 2022; year++) {
            if (moviesByYear.containsKey(year)) {
                // Check each movie in the current year and see if average votes match the given one
                for (int movieID : moviesByYear.get(year)) {
                    Filme currentMovie = moviesDict.get(movieID);
                    int sharedActors = 0;

                    // If year is the same we have to check if movie is older than the given one
                    if (year == date.getYear()
                            && LocalDate.parse(currentMovie.dataLancamento, dateFileFormat).isAfter(date)) {
                        if (currentMovie.mediaVotos == avgVotes && currentMovie.atores != null) {
                            // Checks if has only one shared actor
                            sharedActors = AuxiliaryQueryFunctions.numberOfSharedActors(movieID, actorIDs, moviesDict);
                        }
                    } else {
                        if (currentMovie.mediaVotos == avgVotes && currentMovie.atores != null) {
                            // Checks if has only one shared actor
                            sharedActors = AuxiliaryQueryFunctions.numberOfSharedActors(movieID, actorIDs, moviesDict);
                        }
                    }

                    // Checks if theres only one shared actor
                    if (sharedActors == 1) {
                        // Adds movie title to 'validMovieTitles'
                        validMovieTitles.add(currentMovie.titulo);
                    }
                }
            }
        }

        StringBuilder outputString = new StringBuilder();
        for (int i = 0; i < validMovieTitles.size(); i++) {
             outputString.append(validMovieTitles.get(i));
             if (i != validMovieTitles.size() - 1) {
                 outputString.append("||");
             }
        }


        endTime = System.currentTimeMillis();
        return new QueryResult(outputString.toString(), (endTime - startTime));
    }

    /**
     * 'GET_TOP_N_YEARS_BEST_AVG_VOTES' Query.
     * Returns the N (given number) of years with the best movie average votes between all movies.
     * @param data Query Arguments
     * @param moviesByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns the years with the best average votes
     */
    public static QueryResult getTopNYearsBestAVGVotes(
            String data, HashMap<Integer, ArrayList<Integer>> moviesByYear, HashMap<Integer, Filme> moviesDict
    ) {
        startTime = System.currentTimeMillis();
        // Gets number of movies to output
        int moviesOutputNum = Integer.parseInt(data);

        ArrayList<AVGVotesByYear> votesByYear = new ArrayList<>();

        // Adds each year votes average to 'votesByYear'
        for (Integer movie : moviesByYear.keySet()) {
            float yearVotesAverage = AuxiliaryQueryFunctions.calculateYearVotesAverage(
                    moviesByYear.get(movie), moviesDict);
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
            outputString.append(Math.round(current.avgVotes * 100) / 100.0);  // Outputs a max of 2 decimal places

            // Does not put '\n' in the last year
            if (i != moviesOutputNum - 1) {
                outputString.append('\n');
            }
        }

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString.toString(), (endTime - startTime));
    }

    /**
     * 'DISTANCE_BETWEEN_ACTORS' Query.
     * Returns the distance level that 2 actors are from each other.
     * If both actors participated in the same movie, return level 0.
     * If there's a third actor that collaborated with both actors, returns level 1.
     * If none of the above are true, returns ":(" (sad face).
     * @param data Query Arguments
     * @param people HashMap (KEY: name, VALUE: ArrayList with MovieAssociate's) that stores all people
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns the distance level between the two given actors
     */
    public static QueryResult distanceBetweenActors(
            String data, HashMap<String, ArrayList<MovieAssociate>> people, HashMap<Integer, Filme> moviesDict
    ) {
        startTime = System.currentTimeMillis();
        // Gets query args
        String[] queryArgs = data.split(",");
        String actor1 = queryArgs[0];
        String actor2 = queryArgs[1];

        StringBuilder outputString = new StringBuilder();

        // Gets all movie IDs from 'actor1'
        ArrayList<Integer> actor1MovieIDs = people.get(actor1).get(0).associatedMoviesID;

        // Level 0: the two actors participated in the same movie
        if (AuxiliaryQueryFunctions.actorIsContainedInMovies(actor2, actor1MovieIDs, people)) {
            outputString.append("0");
        // Level 1: there's a third actor that collaborated with both actors
        } else if (AuxiliaryQueryFunctions.thirdActorCollaboration(actor1, actor2, people, moviesDict)) {
            outputString.append("1");
        // Every other case
        } else {
            outputString.append(":(");
        }

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString.toString(), (endTime - startTime));
    }

    /**
     * 'GET_TOP_N_MOVIES_RATIO' Query.
     * Returns the top N (given number) movies with the best VotesAVG/NrOfActors ratio for a given year.
     * @param data Query Arguments
     * @param moviesByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns the movies with the best ratio for the given year
     */
    public static QueryResult getTopNMoviesRatio(
            String data, HashMap<Integer, ArrayList<Integer>> moviesByYear, HashMap<Integer, Filme> moviesDict
    ) {
        startTime = System.currentTimeMillis();
        // Gets query args
        String[] queryArgs = data.split(" ");
        int moviesOutputNum = Integer.parseInt(queryArgs[0]);
        int year = Integer.parseInt(queryArgs[1]);

        // Gets all movie IDs from given 'year'
        ArrayList<Integer> movieIDs = moviesByYear.get(year);

        // Stores movies' ratio
        ArrayList<MovieRatio> moviesRatio = AuxiliaryQueryFunctions.calculateMoviesRatio(movieIDs, moviesDict);

        // Sorts 'moviesRatio' by ratio
        SortingAlgorithms.quickSortByMovieRatio(moviesRatio);

        StringBuilder outputString = new StringBuilder();

        // Checks if there are results
        if (moviesRatio.size() > 0) {
            // Builds output string
            for (int i = 0, pos = moviesRatio.size() - 1; i < moviesOutputNum && i < moviesRatio.size(); i++, pos--) {
                // Gets current movie
                MovieRatio movie = moviesRatio.get(pos);
                outputString.append(movie.title);
                outputString.append(':');
                outputString.append(movie.ratio);

                if (i != moviesRatio.size() - 1 && i != moviesOutputNum - 1) {
                    outputString.append('\n');
                }
            }
        } else {
            // Builds output string
            outputString.append("zerop");
        }

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString.toString(), (endTime - startTime));
    }

    /**
     * 'TOP_6_DIRECTORS_WITHIN_FAMILY' Query.
     * Returns the top 6 directors that do the most directions with family members.
     * Family directions are directions that have at least 2 directors with the same last name.
     * @param data Query Arguments
     * @param moviesByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns the 6 directors with most family directions
     */
    public static QueryResult top6DirectorsWithinFamily(
            String data, HashMap<Integer, ArrayList<Integer>> moviesByYear, HashMap<Integer, Filme> moviesDict
    ) {
        startTime = System.currentTimeMillis();
        // Gets query args
        String[] queryArgs = data.split(" ");
        int rangeStart = Integer.parseInt(queryArgs[0]);
        int rangeEnd = Integer.parseInt(queryArgs[1]);

        // Stores number of directions with family members (KEY: Actor Name, VALUE: Number of directions)
        HashMap<String, Integer> directionsWithFamilyMembers = new HashMap<>();

        // Iterates over all movies in the given range
        for (int year = rangeStart; year <= rangeEnd; year++) {
            // Checks if given year exists in 'moviesByYear'
            if (moviesByYear.containsKey(year)) {
                for (int movieID : moviesByYear.get(year)) {
                    AuxiliaryQueryFunctions.getDirectionsWithFamilyMembers(
                            movieID, moviesDict, directionsWithFamilyMembers);
                }
            }
        }

        ArrayList<DirectorsFamily> familyDirectionsByDirector = new ArrayList<>();

        // Adds each entry in 'directionsWithFamilyMembers' to 'familyDirectionsByDirector'
        for (String director : directionsWithFamilyMembers.keySet()) {
            familyDirectionsByDirector.add(new DirectorsFamily(director, directionsWithFamilyMembers.get(director)));
        }

        // Sorts 'familyDirectionsByDirector' by Number of family directions
        SortingAlgorithms.quickSortByFamilyDirections(familyDirectionsByDirector);

        StringBuilder outputString = new StringBuilder();

        // Size of 'familyDirectionsByDirector' ArrayList
        int dirListSize = familyDirectionsByDirector.size();

        if (dirListSize > 0) {
            for (int pos = dirListSize - 1, outputNum = 0; pos >= 0 && outputNum < 6; pos--, outputNum++) {
                // Gets current movie
                DirectorsFamily director = familyDirectionsByDirector.get(pos);
                outputString.append(director.name);
                outputString.append(':');
                outputString.append(director.familyDirections);

                if (outputNum < 5 && outputNum != dirListSize - 1) {
                    outputString.append('\n');
                }
            }
        } else {
            outputString.append("NEM UM");
        }

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString.toString(), (endTime - startTime));
    }

    /**
     * 'GET_TOP_ACTOR_YEAR' Query.
     * Returns the actor with the most appearances in movies from the given year.
     * @param data Query Arguments
     * @param moviesByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns the actor with the greatest movie count for the given year
     */
    public static QueryResult getTopActorYear(
            String data, HashMap<Integer, ArrayList<Integer>> moviesByYear, HashMap<Integer, Filme> moviesDict
    ) {
        startTime = System.currentTimeMillis();
        // Gets query args
        int year = Integer.parseInt(data);

        // Gets all movieIDs from the given year
        ArrayList<Integer> movieIDs = moviesByYear.get(year);

        // Stores the number of movies by actor for the given 'year'
        HashMap<String, Integer> moviesByActor = new HashMap<>();

        // Counts the number of movie appearances by actor in the given year and store them in 'moviesByActor'
        AuxiliaryQueryFunctions.countNumberOfMoviesByActor(movieIDs, moviesDict, moviesByActor);

        String mostMoviesActor = "";

        // Checks which actor has the most movie appearances
        for (String actor : moviesByActor.keySet()) {
            if (mostMoviesActor.isEmpty()) {
                mostMoviesActor = actor;
            } else if (moviesByActor.get(actor) > moviesByActor.get(mostMoviesActor)) {
                mostMoviesActor = actor;
            }
        }

        // Builds output string
        String outputString = mostMoviesActor + ";" + moviesByActor.get(mostMoviesActor);

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString, (endTime - startTime));
    }

    /**
     * 'INSERT_ACTOR' Query.
     * Inserts a new actor in all the necessary data structures to be used in future queries.
     * The actor needs to be valid (have a unique ID and the movie needs to exist).
     * @param data Query Arguments
     * @param people HashMap (KEY: name, VALUE: ArrayList with MovieAssociate's) that stores all people
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @param actorsByID HashMap (KEY: Actor ID, VALUE: Actor Name) with all actors
     * @return Returns "OK" message on success and "Erro" message on failure
     */
    public static QueryResult insertActor(
            String data,
            HashMap<String ,ArrayList<MovieAssociate>> people,
            HashMap<Integer, Filme> moviesDict,
            HashMap<Integer, String> actorsByID
    ) {
        startTime = System.currentTimeMillis();
        // Gets query args
        String[] queryArgs = data.split(";");
        int id = Integer.parseInt(queryArgs[0].strip());
        String name = queryArgs[1].strip();
        char gender = queryArgs[2].strip().charAt(0);
        int movieID = Integer.parseInt(queryArgs[3].strip());

        StringBuilder outputString = new StringBuilder();

        // Creates new 'MovieAssociate' instance
        MovieAssociate person = new MovieAssociate();
        person.id = id;
        person.name = name;
        person.gender = gender;
        person.type = "ACTOR";
        person.associatedMoviesID = new ArrayList<>();
        person.associatedMoviesID.add(movieID);

        // Creates new 'Pessoa' instance
        Pessoa pessoa = new Pessoa(id, name, gender);

        // Gets movie from 'moviesDict'
        Filme movie = moviesDict.get(movieID);

        boolean existsActorID = actorsByID.containsKey(id);
        boolean actorIDisFromGivenActor = false;

        if (actorsByID.get(id) != null) {
            actorIDisFromGivenActor = actorsByID.get(id).equals(name);
        }

        // If a person with the given 'id' does not exist and the movie exists, add new actor
        if (movie != null && !existsActorID) {
            // Adds actor to 'people'
            if (people.get(name) == null) {
                ArrayList<MovieAssociate> peopleList = new ArrayList<>();
                peopleList.add(person);
                people.put(name, peopleList);
            } else {
                people.get(name).add(person);
            }

            // Adds actor to the correspondent movie
            movie.atores.add(pessoa);

            // Adds actor to 'actorsByID'
            actorsByID.put(id, name);

            // Builds output string
            outputString.append("OK");
        } else if (movie != null && actorIDisFromGivenActor) {
            // In case the person already exists, just add the movie to it if it is not already there
            for (MovieAssociate movieAssociate : people.get(name)) {
                if (movieAssociate.id == id && movieAssociate.type.equals("ACTOR")) {
                    movieAssociate.associatedMoviesID.add(movieID);
                }
            }

            // Adds actor to the correspondent movie
            movie.atores.add(pessoa);

            // Builds output string
            outputString.append("OK");
        } else {
            // If ID belongs to a different person or the movie does not exist, outputs 'Erro' message
            // Builds output string
            outputString.append("Erro");
        }

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString.toString(), (endTime - startTime));
    }

    /**
     * 'REMOVE_ACTOR' Query.
     * Removes an existing actor from all the necessary data structures so it won't appear in future queries.
     * The given actor needs to be valid, i.e. the ID must exist.
     * @param data Query Arguments
     * @param people HashMap (KEY: name, VALUE: ArrayList with MovieAssociate's) that stores all people
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @param actorsByID HashMap (KEY: Actor ID, VALUE: Actor Name) with all actors
     * @return Returns "OK" message on success and "Erro" message on failure
     */
    public static QueryResult removeActor(
            String data,
            HashMap<String ,ArrayList<MovieAssociate>> people,
            HashMap<Integer, Filme> moviesDict,
            HashMap<Integer, String> actorsByID
    ) {
        startTime = System.currentTimeMillis();
        // Gets query args
        int id = Integer.parseInt(data);

        StringBuilder outputString = new StringBuilder();

        boolean idExists = actorsByID.containsKey(id);
        // Gets 'MovieAssociate' object associated with 'id'
        MovieAssociate person = AuxiliaryQueryFunctions.getMovieAssociateByIDAndName(
                id, actorsByID.get(id), "ACTOR", people);

        if (idExists) {
            // Gets all movie IDs the actor is associated with
            ArrayList<Integer> movieIDs = person.associatedMoviesID;

            // Removes actor from 'people'
            AuxiliaryQueryFunctions.removePersonFromPeopleDict(person, people);

            // Removes actor from all the movies he's associated with
            AuxiliaryQueryFunctions.removeActorFromAllMoviesByID(id, movieIDs, moviesDict);

            // Builds output string
            outputString.append("OK");
        } else {
            // Builds output string
            outputString.append("Erro");
        }

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString.toString(), (endTime - startTime));
    }

    /**
     * 'GET_DUPLICATE_LINES_YEAR' Query.
     * Returns the duplicate people lines from 'deisi_people.txt' file.
     * @param data Query Arguments
     * @param duplicateLinesYear HashMap (KEY: Year, VALUE: ArrayList with all the duplicate lines)
     * @return Returns the duplicate lines from people file
     */
    public static QueryResult getDuplicateLinesYear(
            String data, HashMap<Integer, ArrayList<String>> duplicateLinesYear
    ) {
        startTime = System.currentTimeMillis();
        // Gets given year
        int year = Integer.parseInt(data);

        StringBuilder outputString = new StringBuilder();

        if (duplicateLinesYear.containsKey(year) && duplicateLinesYear.get(year).size() > 0) {
            // Gets 'duplicateLinesByYear' entry for the given year
            ArrayList<String> duplicateLines = duplicateLinesYear.get(year);

            // If there are results, append each of them to 'outputString'
            for (int i = 0; i < duplicateLines.size(); i++) {
                outputString.append(duplicateLines.get(i));

                if (i != duplicateLines.size() - 1) {
                    outputString.append('\n');
                }
            }
        }

        // If there are no results, outputs an empty string

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString.toString(), (endTime - startTime));
    }

    /**
     * 'TOP_10_MOST_EXPENSIVE_MOVIES_YEAR' Query.
     * Returns the top 10 movies with the largest budget for the given year.
     * Input Format: "TOP_10_MOST_EXPENSIVE_MOVIES_YEAR <Year>"
     * Output Format: "<MovieTitle> - Budget: $<MovieBudget>" (separated by '\n', newline character)
     * @param data Query Arguments
     * @param moviesByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     * @return Returns the most expensive movies from the given year
     */
    public static QueryResult topNMostExpensiveMoviesYear(
            String data, HashMap<Integer, ArrayList<Integer>> moviesByYear, HashMap<Integer, Filme> moviesDict
    ) {
        startTime = System.currentTimeMillis();
        // Gets query args
        String[] queryArgs = data.split(" ");
        int year = Integer.parseInt(queryArgs[0]);

        // Number of movies to output
        int outputNum = 10;

        // Gets movie IDs from given 'year'
        ArrayList<Integer> movieIDs = moviesByYear.get(year);

        // Stores all 'MovieBudget' objects for each movie in 'movieIDs'
        ArrayList<MovieBudget> moviesBudget = new ArrayList<>();

        if (movieIDs != null) {
            for (Integer movieID : movieIDs) {
                // Gets current movie being checked
                Filme movie = moviesDict.get(movieID);

                // Adds a new 'MovieBudget' object with current movie data to 'moviesBudget'
                MovieBudget movieBudget = new MovieBudget(movie.titulo, movie.orcamento);
                moviesBudget.add(movieBudget);
            }
        }

        // Sorts all 'MovieBudget' objects in 'moviesBudget' by 'budget' property
        SortingAlgorithms.quickSortByMovieBudget(moviesBudget);

        StringBuilder outputString = new StringBuilder();

        // Checks if there are results
        if (moviesBudget.size() > 0) {
            // Builds output string
            for (int i = 0, pos = moviesBudget.size() - 1; i < outputNum && i < moviesBudget.size(); i++, pos--) {
                // Gets current movie
                MovieBudget movie = moviesBudget.get(pos);
                outputString.append(movie.title);
                outputString.append(" - ");
                outputString.append("Budget: ");
                outputString.append('$');
                outputString.append(movie.budget);

                if (i != moviesBudget.size() - 1 && i != outputNum - 1) {
                    outputString.append('\n');
                }
            }
        } else {
            // Builds output string
            outputString.append("No movies found! LULz");
        }

        endTime = System.currentTimeMillis();
        return new QueryResult(outputString.toString(), (endTime - startTime));
    }
}
