package pt.ulusofona.deisi.aed.deisiflix;

public class SearchAlgorithms {
    // Binary Search Algorithm
    static int binarySearchMovieByID(Filme[] movies, int id) {
        int left = 0, right = movies.length - 1;

        while (left <= right) {
            int middle = (left + right) / 2;

            if (movies[middle].id == id) {
                return middle;
            }

            if (movies[middle].id < id) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
        return -1;
    }
}
