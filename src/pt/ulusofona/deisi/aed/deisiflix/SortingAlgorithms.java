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

    // Selection Sort Algorithm 'GET_MOVIES_ACTOR_YEAR' query
    // Sort movies by Date (descending)
    static ArrayList<QueryFunctions.MovieActorYear> selSortDateByDescendingOrder(ArrayList<QueryFunctions.MovieActorYear> movies) {
        int greatestSortedPos = -1;

        while (greatestSortedPos < movies.size() - 1) {
            int greatestPos = greatestSortedPos + 1;

            for (int i = greatestPos + 1; i < movies.size(); i++) {
                if (movies.get(i).date.isBefore(movies.get(greatestPos).date)) {
                    greatestPos = i;
                }
            }

            greatestSortedPos++;

            if (greatestSortedPos != greatestPos) {
                QueryFunctions.MovieActorYear temp = movies.get(greatestSortedPos);
                movies.add(greatestSortedPos, movies.get(greatestPos));
                movies.add(greatestPos, temp);
            }
        }
        return movies;
    }
}
