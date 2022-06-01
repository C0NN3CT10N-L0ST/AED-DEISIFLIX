package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;

public class ReaderFunctions {
    /**
     * Adds 'Pessoa' object to the correspondent movie field (based on the person type) in the movies array.
     * @param person Pessoa object to be added
     * @param personType Person type
     * @param movieID ID of the movie that were the object will be added
     * @param movies Array with all movies (sorted by ID)
     */
    public static void addPersonToMovies(Pessoa person, String personType, int movieID, Filme[] movies) {
        // Gets position of 'idMovie' in 'sortedMovies'
        int moviePos = SearchAlgorithms.binarySearchMovieByID(movies, movieID);

        // Checks if 'idMovie' exists in 'movies', if not, do nothing
        if (moviePos == -1) { return; }

        Filme movie = movies[moviePos];

        // Adds 'ACTOR'
        if (personType.equals("ACTOR")) {
            // Checks if 'atores' is null, if it is, creates a new 'ArrayList'
            if (movie.atores == null) {
                movie.atores = new ArrayList<>();
            }

            // Adds person to 'atores'
            movie.atores.add(person);
        } else {  // ADDS 'DIRECTOR'
            // Checks if 'realizadores' is null, if it is, creates a new 'ArrayList'
            if (movie.realizadores == null) {
                movie.realizadores = new ArrayList<>();
            }

            // Adds person to 'realizadores'
            movie.realizadores.add(person);
        }
    }
}
