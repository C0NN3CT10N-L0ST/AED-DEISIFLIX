package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AuxiliaryQueryFunctions {
    /*
        Checks if all actors from movie with ID -> 'movieID' are already on 'actors' ArrayList.
        If not, adds them.
     */
    public static void getUniqueMovieActors(int movieID, ArrayList<String> actors, Filme[] sortedMovies) {
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

    /*
        Returns whether 'actors' contains all 'actorNames'
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

    public static void calculateGenderPercentualDiscrepancy(
            int year,
            HashMap<Integer, ArrayList<Integer>> moviesByYear,
            Filme[] sortedMovies,
            ArrayList<QueryFunctions.MovieGenderBias> moviesGenderBias
    ) {
        // Calculates Percentual Discrepancy for every movie in the given 'year' and stores it in 'moviesGenderBias'
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

                    // Adds movie data to 'moviesGenderBias'
                    moviesGenderBias.add(new QueryFunctions.MovieGenderBias(currentMovie.titulo, discrepancy, predominantGender));
                }
            }
        }
    }
}
