package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;
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
}
