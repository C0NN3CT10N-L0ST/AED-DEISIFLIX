package pt.ulusofona.deisi.aed.deisiflix;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class AuxiliaryQueryFunctions {
    /**
     * Returns all the movies in which the year matches the given one.
     * @param year The given year
     * @param dateFormat Date format of the 'Filme' release date
     * @param movies List of movies to check
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all existing movies
     * @param moviesActorYears ArrayList with all 'MovieActorYear'
     */
    public static void getMoviesFromYear(
            int year,
            DateTimeFormatter dateFormat,
            ArrayList<Integer> movies,
            HashMap<Integer, Filme> moviesDict,
            ArrayList<QueryFunctions.MovieActorYear> moviesActorYears
    ) {
        for (Integer movieID : movies) {
            // Gets current 'Filme' object being checked
            Filme movie = moviesDict.get(movieID);
            if (movie != null) {
                LocalDate movieDate = LocalDate.parse(movie.dataLancamento, dateFormat);
                int movieYear = movieDate.getYear();

                // Adds 'movie' to 'moviesActorYear' in case the 'year' matches
                if (movieYear == year) {
                    String title = movie.titulo;
                    moviesActorYears.add(new QueryFunctions.MovieActorYear(title, movieDate));
                }
            }
        }
    }

    /**
     * Stores all actors from all movies from a particular year in an HashSet.
     * @param year The given year
     * @param moviesByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @param actors HashSet to store the actor names
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all existing movies
     */
    public static void getUniqueMovieActors(
            int year,
            HashMap<Integer, ArrayList<Integer>> moviesByYear,
            HashSet<String> actors,
            HashMap<Integer, Filme> moviesDict
    ) {
        // Gets all movie IDs from given year
        ArrayList<Integer> movies = moviesByYear.get(year);

        // Adds every actor of every movie to 'actors' HashSet
        for (int movieID : movies) {
            // Gets 'Filme' object
            Filme movie = moviesDict.get(movieID);

            // First it checks if 'movie' exists in 'moviesDict' and if 'atores' is not null
            if (movie != null && movie.atores != null) {
                for (Pessoa actor : movie.atores) {
                    actors.add(actor.nome);
                }
            }
        }
    }

    /**
     * Returns whether an ArrayList contains all actor names provided.
     * @param actors ArrayList where all actors are stored
     * @param actorNames String with all actor names separated by semicolons
     * @return Returns whether all actors are contained in the ArrayList
     */
    public static boolean containsActors(ArrayList<Pessoa> actors, String actorNames) {
        // Gets names from 'actorNames' (String with names separated by semicolons)
        String[] names = actorNames.split(";");
        // Gets the number of actors
        int actorsNum = names.length;
        // Counts the number of actor names in 'actors'
        int actorsCount = 0;

        // Iterate over the ArrayList
        for (Pessoa actor : actors) {
            for (String name : names) {
                if (Objects.equals(actor.nome, name)) {
                    actorsCount++;
                }
            }
        }
        // If 'actorsCount' matches 'actorsNum' all actors are in 'actors'
        return actorsCount == actorsNum;
    }

    /**
     * Calculates Gender Percentual Discrepancy for all movies in the given year.
     * @param year The given year
     * @param moviesByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all existing movies
     * @param moviesGenderBias ArrayList where all calculated value will be stored
     */
    public static void calculateGenderPercentualDiscrepancy(
            int year,
            HashMap<Integer, ArrayList<Integer>> moviesByYear,
            HashMap<Integer, Filme> moviesDict,
            ArrayList<QueryFunctions.MovieGenderBias> moviesGenderBias
    ) {
        // Iterates through every movie in the given 'year'
        for (Integer movieID : moviesByYear.get(year)) {
            // Gets 'Filme' object
            Filme movie = moviesDict.get(movieID);

            // Verifies that 'movie' exists in 'moviesDict' and that 'atores' is not null
            if (movie != null && movie.atores != null) {
                int totalActors = movie.atores.size();
                boolean hasEnoughPeople = totalActors > 10;

                // Only does the calculation if movie has more than 10 actors
                if (hasEnoughPeople) {
                    int actorsNum = 0;
                    int actressesNum = 0;
                    int discrepancy;
                    char predominantGender;

                    // Iterates over all people in 'atores' for each movie and counts them based on gender
                    for (Pessoa person : movie.atores) {
                        if (person.genero == 'M') {
                            actorsNum++;
                        }

                        if (person.genero == 'F') {
                            actressesNum++;
                        }
                    }

                    // Checks which is the predominant gender and calculates percentual discrepancy based on that
                    if (actorsNum > actressesNum) {
                        discrepancy = (int) Math.round(actorsNum * 100.0 / totalActors);
                        predominantGender = 'M';
                    } else {
                        discrepancy = (int) Math.round(actressesNum * 100.0 / totalActors);
                        predominantGender = 'F';
                    }

                    // Adds movie gender data to 'moviesGenderBias'
                    moviesGenderBias.add(new QueryFunctions.MovieGenderBias(
                            movie.titulo, discrepancy, predominantGender));
                }
            }
        }
    }

    /**
     * Calculates the movie average votes from all movies for the given year.
     * @param movieIDs ArrayList that contains all the movie IDs for the given year
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all existing movies
     * @return Returns movies' average votes for the given year
     */
    public static float calculateYearVotesAverage(ArrayList<Integer> movieIDs, HashMap<Integer, Filme> moviesDict) {
        float totalVotesAverage = 0.0F;

        // Adds each movie votesAverage to 'totalVotesAverage'
        for (Integer movieID : movieIDs) {
            // Gets movie position in 'sortedMovies'
            Filme movie = moviesDict.get(movieID);

            if (movie != null) {
                totalVotesAverage += movie.mediaVotos;
            }
        }
        return totalVotesAverage / movieIDs.size();
    }

    /**
     * Returns whether an actor has participated in a set of given movies.
     * @param actor The given actor name
     * @param movies Set of movies to check
     * @param people HashMap (KEY: name, VALUE: MovieAssociate) that stores all people
     * @return Returns whether the given actor participated in at least one of the given movies set.
     */
    public static boolean actorIsContainedInMovies(
            String actor, ArrayList<Integer> movies, HashMap<String, ArrayList<MovieAssociate>> people
    ) {
        // Gets all movie IDs actor was in
        ArrayList<Integer> actorMovies = people.get(actor).get(0).associatedMoviesID;

        // Checks if theres at least one movie in common between 'actorMovies' and 'movies'
        for (int movieID : movies) {
            if (actorMovies.contains(movieID)) {
                return true;
            }
        }

        // If there are no movies in common returns 'false'
        return false;
    }

    /**
     * Checks if there's a third actor that has participated in at least one movie with actor1 and actor2.
     * @param actor1 The first given actor
     * @param actor2 The second given actor
     * @param people HashMap (KEY: name, VALUE: MovieAssociate) that stores all people
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all existing movies
     * @return Returns whether there is a third actor in common with actor1 and actor2
     */
    public static boolean thirdActorCollaboration(
            String actor1,
            String actor2,
            HashMap<String, ArrayList<MovieAssociate>> people,
            HashMap<Integer, Filme> moviesDict) {
        // Gets all movies that 'actor1' has been part of
        ArrayList<Integer> actor1MovieIDs = people.get(actor1).get(0).associatedMoviesID;

        // Stores all actors from each of 'actor1MovieIDs'
        HashSet<String> actors = new HashSet<>();

        // Gets all actors from each of 'actor1MovieIDs'
        for (int movieID : actor1MovieIDs) {
            // Adds all actor names to 'actors' HashSet
            for (Pessoa actor : moviesDict.get(movieID).atores) {
                actors.add(actor.nome);
            }
        }

        // Gets all movies that 'actor2' has been part of
        ArrayList<Integer> actor2MovieIDs = people.get(actor2).get(0).associatedMoviesID;

        // Checks if there's at least one actor from 'actors' in 'actor2' movies
        for (Integer movieID : actor2MovieIDs) {
            // Gets current 'Filme' being checked
            Filme movie = moviesDict.get(movieID);

            // If there's an actor in common between 'actor2' movies and 'actor1' movies, returns true
            for (Pessoa actor : movie.atores) {
                if (actors.contains(actor.nome)) {
                    return true;
                }
            }
        }
        // If there's no match, returns 'false'
        return false;
    }

    /**
     * Calculates the VotesAVG/NrOfActors ratio for each of the given movies.
     * @param movieIDs Set of movieIDs to calculate ratio
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all existing movies
     * @return Returns a 'MovieRatio' object for each movie as an ArrayList
     */
    public static ArrayList<QueryFunctions.MovieRatio> calculateMoviesRatio(
            ArrayList<Integer> movieIDs, HashMap<Integer, Filme> moviesDict
    ) {
        // Stores 'MovieRatio' obejct for each movie
        ArrayList<QueryFunctions.MovieRatio> output = new ArrayList<>();

        // Calculates the AVGVotes/NrOfActor ratio for each movie in 'movieIDs'
        for (Integer movieID : movieIDs) {
            // Gets current movie being checked
            Filme movie = moviesDict.get(movieID);
            // Calculates ratio
            if (movie.atores != null) {
                double ratio = movie.mediaVotos / movie.atores.size();

                // Adds new 'MovieRatio' object to 'output'
                output.add(new QueryFunctions.MovieRatio(movie.titulo, ratio));
            }
        }
        return output;
    }

    /**
     * Counts the number of movies each actor participated in for the given list of movie IDs
     * and stores them in an HashMap.
     * @param movieIDs Set of movies to count the actor appearances
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all existing movies
     * @param moviesByActor HashMap (KEY: actor name, VALUE: number of movie appearances) with
     *                      number of appearances by actor
     */
    public static void countNumberOfMoviesByActor(
            ArrayList<Integer> movieIDs, HashMap<Integer, Filme> moviesDict, HashMap<String, Integer> moviesByActor
    ) {
        // Iterates over each movie in 'movieIDs'
        for (Integer movieID : movieIDs) {
            // Gets current movie being checked
            Filme movie = moviesDict.get(movieID);
            if (movie.atores != null) {
                // If the entry for the given actor exists, increment its count by 1, if not, create a new entry
                for (Pessoa actor : movie.atores) {
                    if (moviesByActor.containsKey(actor.nome)) {
                        int currentCount = moviesByActor.get(actor.nome);
                        moviesByActor.put(actor.nome, ++currentCount);
                    } else {
                        moviesByActor.put(actor.nome, 1);
                    }
                }
            }
        }
    }

    /**
     * Adds a person to an HashMap with all people.
     * @param person The given 'MovieAssociate' object
     * @param people HashMap (KEY: name, VALUE: MovieAssociate) that stores all people
     */
    public static void addPersonToPeopleDict(MovieAssociate person, HashMap<String, ArrayList<MovieAssociate>> people) {
        // Checks if an entry for the person name already exists and if the person already exists as well
        if (people.containsKey(person.name)) {
            boolean personAlreadyExists = false;
            for (MovieAssociate movieAssociate : people.get(person.name)) {
                if (movieAssociate.id == person.id && movieAssociate.type.equals(person.type)) {
                    personAlreadyExists = true;
                }
            }

            // If person already exists, do nothing, otherwise, adds new person
            if (!personAlreadyExists) {
                people.get(person.name).add(person);
            }
        } else {
            // If person does not yet exist, just add it
            ArrayList<MovieAssociate> movieAssociates = new ArrayList<>();
            movieAssociates.add(person);
            people.put(person.name, movieAssociates);
        }
    }

    /**
     * Returns the 'MovieAssociate' object with the given ID and Name.
     * If the object does not exist, returns null.
     * @param id The given ID
     * @param name The given Name
     * @param type The given Type ("ACTOR" OR "DIRECTOR")
     * @param people HashMap (KEY: name, VALUE: MovieAssociate) that stores all people
     * @return Returns a 'MovieAssociate' object or null.
     */
    public static MovieAssociate getMovieAssociateByIDAndName(
            int id, String name, String type, HashMap<String, ArrayList<MovieAssociate>> people
    ) {
        // Create new 'MovieAssociate' object to store person
        MovieAssociate person = null;

        if (people.containsKey(name)) {
            for (MovieAssociate movieAssociate : people.get(name)) {
                if (movieAssociate.id == id && movieAssociate.type.equals(type)) {
                    person = movieAssociate;
                }
            }
        }

        return person;
    }

    /**
     * Removes a person from HashMap (KEY: Person Name, VALUE: ArrayList of MovieAssociate's objects) with all people.
     * @param person The given 'MovieAssociate' object
     * @param people HashMap (KEY: name, VALUE: MovieAssociate) that stores all people
     */
    public static void removePersonFromPeopleDict(
            MovieAssociate person, HashMap<String, ArrayList<MovieAssociate>> people
    ) {
        // First checks if entry exists
        if (people.containsKey(person.name)) {
            // Gets person position in the ArrayList of the correspondent key
            int personPos = -1;  // If person does not exist, 'personPos' will be '-1'

            // Gets people ArrayList
            ArrayList<MovieAssociate> peopleList = people.get(person.name);

            for (int i = 0; i < peopleList.size(); i++) {
                if (peopleList.get(i).id == person.id && peopleList.get(i).type.equals(person.type)) {
                    personPos = i;
                }
            }

            // Removes 'person' from 'people'
            peopleList.remove(personPos);
        }
    }

    /**
     * Removes an actor from all the movies he's associated with.
     * @param id The given actor ID
     * @param movieIDs ArrayList with all the movie IDs the actor is associated with
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all existing movies
     */
    public static void removeActorFromAllMoviesByID(
            int id, ArrayList<Integer> movieIDs, HashMap<Integer, Filme> moviesDict
    ) {
        for (Integer movieID : movieIDs) {
            // Gets current movie
            Filme movie = moviesDict.get(movieID);

            int moviePos = -1;

            // Gets movie index in the current movie 'atores' ArrayList
            for (int i = 0; i < movie.atores.size(); i++) {
                if (movie.atores.get(i).id == id) {
                    moviePos = i;
                }
            }

            // Removes actor from movie 'atores' ArrayList
            if (moviePos != -1) {
                movie.atores.remove(moviePos);
            }
        }
    }

    /**
     * Returns the number of shared actors between a set of actor IDs and the actors from a movie.
     * @param movieID The given movie ID to check
     * @param actorIDs An ArrayList with all the actor IDs to check
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all existing movies
     * @return Returns the number of shared actors
     */
    public static int numberOfSharedActors(
            int movieID, ArrayList<Integer> actorIDs, HashMap<Integer, Filme> moviesDict
    ) {
        int sharedActors = 0;

        if (moviesDict.containsKey(movieID)) {
            // Checks how many actors from the movie with 'movieID' are contained in 'actors'
            for (Pessoa actor : moviesDict.get(movieID).atores) {
                if (actorIDs.contains(actor.id)) {
                    sharedActors++;
                }
            }
        }
        return sharedActors;
    }

    public static void getDirectionsWithFamilyMembers(
            int movieID, HashMap<Integer, Filme> moviesDict, HashMap<String, Integer> directionsWithFamilyMembers) {
        // Stores all director last names in an arraylist and checks if at least 2 of them match
        // Checks if movie with ID 'movieID' exists and if the movie contains directors
        if (moviesDict.containsKey(movieID) && moviesDict.get(movieID).realizadores != null) {
            // Gets current movie
            Filme movie = moviesDict.get(movieID);

            // Checks if each director has family members in the movie and increments its count in the HashMap
            for (int i = 0; i < movie.realizadores.size(); i++) {
                String directorName = movie.realizadores.get(i).nome;
                String[] directorSplittedName = movie.realizadores.get(i).nome.split(" ");
                String lastName = directorSplittedName[directorSplittedName.length - 1];

                // Checks if any of the other directors' last names match the last name of the current one
                for (int j = 0; j < movie.realizadores.size(); j++) {
                    if (j != i) {
                        // Gets the last name of the current director being checked
                        String[] currentDirectorSplittedName = movie.realizadores.get(j).nome.split(" ");
                        String currentlastName = currentDirectorSplittedName[currentDirectorSplittedName.length - 1];

                        if (currentlastName.equals(lastName)) {
                            if (directionsWithFamilyMembers.containsKey(directorName)) {
                                // Increments the director count by
                                int previousCount = directionsWithFamilyMembers.get(directorName);
                                directionsWithFamilyMembers.put(directorName, previousCount + 1);
                            } else {
                                // Creates new HashMap entry for the director
                                directionsWithFamilyMembers.put(directorName, 1);
                            }
                        }
                    }
                }
            }
        }
    }
}
