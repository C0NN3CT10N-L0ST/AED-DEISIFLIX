package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AuxiliaryQueryFunctions {
    /**
     * Stores all unique actors from a particular movie in an ArrayList.
     * @param movieID The movie ID
     * @param actors ArrayList to store the actors
     * @param sortedMovies Array that contains all movies (sorted by ID)
     */
    public static void getUniqueMovieActors(int movieID, ArrayList<String> actors, Filme[] sortedMovies) {
        // Gets movie position in 'sortedMovies'
        int moviePos = SearchAlgorithms.binarySearchMovieByID(sortedMovies, movieID);

        // First it checks if 'movieID' exists in 'sortedMovies'
        if (moviePos != -1) {
            for (Pessoa actor : sortedMovies[moviePos].atores) {
                // Checks if actor name is already in 'actors', if not, adds it
                if (!actors.contains(actor.nome)) {
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
     * @param year the given year
     * @param moviesByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @param sortedMovies Array that contains all movies (sorted by ID)
     * @param moviesGenderBias ArrayList where all calculated value will be stored
     */
    public static void calculateGenderPercentualDiscrepancy(
            int year,
            HashMap<Integer, ArrayList<Integer>> moviesByYear,
            Filme[] sortedMovies,
            ArrayList<QueryFunctions.MovieGenderBias> moviesGenderBias
    ) {
        // Iterates through every movie in the given 'year'
        for (Integer movieID : moviesByYear.get(year)) {
            // Get 'movieID' position in 'sortedMovies'
            int moviePos = SearchAlgorithms.binarySearchMovieByID(sortedMovies, movieID);

            // Verifies that 'movieID' exists in 'sortedMovies' and that 'atores' is not null
            if (moviePos != -1 && sortedMovies[moviePos].atores != null) {
                Filme currentMovie = sortedMovies[moviePos];
                int totalActors = currentMovie.atores.size();
                boolean hasEnoughPeople = totalActors >= 10;

                // Only does the calculation if movie has more than 10 actors
                if (hasEnoughPeople) {
                    int actorsNum = 0;
                    int actressesNum = 0;
                    int discrepancy;
                    char predominantGender;

                    // Iterates over all people in 'atores' for each movie and counts them based on gender
                    for (Pessoa person : currentMovie.atores) {
                        if (person.genero == 'M') {
                            actorsNum++;
                        }

                        if (person.genero == 'F') {
                            actressesNum++;
                        }
                    }

                    // Checks which is the predominant gender and calculates percentual discrepancy based on that
                    if (actorsNum > actressesNum) {
                        discrepancy = actorsNum * 100 / totalActors;
                        predominantGender = 'M';
                    } else {
                        discrepancy = actressesNum * 100 / totalActors;
                        predominantGender = 'F';
                    }

                    // Adds movie gender data to 'moviesGenderBias'
                    moviesGenderBias.add(new QueryFunctions.MovieGenderBias(currentMovie.titulo, discrepancy, predominantGender));
                }
            }
        }
    }
}
