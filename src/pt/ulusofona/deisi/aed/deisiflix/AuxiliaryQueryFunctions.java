package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;

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
}
