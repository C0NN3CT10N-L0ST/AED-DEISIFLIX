package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;

public class SortingAlgorithms {
    // QuickSort Partition Algorithm
    private static int partition(Filme[] movies, int left, int right) {
        Filme pivot = movies[right];
        int leftIndex = left;
        int rightIndex = right - 1;

        while (leftIndex <= rightIndex) {
            if (movies[leftIndex].id > pivot.id && movies[rightIndex].id < pivot.id) {
                Filme temp = movies[leftIndex];
                movies[leftIndex] = movies[rightIndex];
                movies[rightIndex] = temp;
            }

            if (movies[leftIndex].id <= pivot.id) {
                leftIndex++;
            }

            if (movies[rightIndex].id >= pivot.id) {
                rightIndex--;
            }
        }

        movies[right] = movies[leftIndex];
        movies[leftIndex] = pivot;
        return leftIndex;
    }

    // QuickSort Algorithm
    private static Filme[] quickSortMoviesByID(Filme[] movies, int left, int right) {
        if (left < right) {
            int pivotPos = partition(movies, left, right - 1);

            movies = quickSortMoviesByID(movies, left, pivotPos);
            movies = quickSortMoviesByID(movies, pivotPos + 1, right);
        }
        return movies;
    }

    // QuickSort Algorithm
    static void quickSortMoviesByID(Filme[] movies) {
        quickSortMoviesByID(movies, 0, movies.length);
    }
}
