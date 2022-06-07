package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;

public class SortingAlgorithms {
    // QuickSort Partition Algorithm
    private static int partitionMoviesByID(Filme[] movies, int left, int right) {
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
            int pivotPos = partitionMoviesByID(movies, left, right - 1);

            movies = quickSortMoviesByID(movies, left, pivotPos);
            movies = quickSortMoviesByID(movies, pivotPos + 1, right);
        }
        return movies;
    }

    // QuickSort Algorithm
    public static void quickSortMoviesByID(Filme[] movies) {
        quickSortMoviesByID(movies, 0, movies.length);
    }

    // Selection Sort Algorithm 'GET_MOVIES_ACTOR_YEAR' query
    // Sort movies by Date (descending)
    public static void selSortDateByDescendingOrder(ArrayList<QueryFunctions.MovieActorYear> movies) {
        int greatestSortedPos = -1;

        while (greatestSortedPos < movies.size() - 1) {
            int greatestPos = greatestSortedPos + 1;

            for (int i = greatestPos + 1; i < movies.size(); i++) {
                if (movies.get(i).date.isAfter(movies.get(greatestPos).date)) {
                    greatestPos = i;
                }
            }

            greatestSortedPos++;

            if (greatestSortedPos != greatestPos) {
                QueryFunctions.MovieActorYear temp = movies.get(greatestSortedPos);
                movies.set(greatestSortedPos, movies.get(greatestPos));
                movies.set(greatestPos, temp);
            }
        }
    }

    // Selection Sort Algorithm for 'TOP_MOVIES_WITH_GENDER_BIAS' query
    // Sort movies by Discrepancy Percentage (descending)
    public static void selSortGenderBiasDescending(ArrayList<QueryFunctions.MovieGenderBias> movies) {
        int greatestSortedPos = -1;

        while (greatestSortedPos < movies.size() - 1) {
            int greatestPos = greatestSortedPos + 1;

            for (int i = greatestPos + 1; i < movies.size(); i++) {
                if (movies.get(i).discrepancyPercentage > movies.get(greatestPos).discrepancyPercentage) {
                    greatestPos = i;
                }
            }

            greatestSortedPos++;

            if (greatestSortedPos != greatestPos) {
                QueryFunctions.MovieGenderBias temp = movies.get(greatestSortedPos);
                movies.set(greatestSortedPos, movies.get(greatestPos));
                movies.set(greatestPos, temp);
            }
        }
    }

    // QuickSort Partition Algorithm for 'GET_TOP_N_YEARS_BEST_AVG_VOTES'
    private static int paritionByAVGVotes(ArrayList<QueryFunctions.AVGVotesByYear> votes, int left, int right) {
        QueryFunctions.AVGVotesByYear pivot = votes.get(right);
        int leftIndex = left;
        int rightIndex = right - 1;

        while (leftIndex <= rightIndex) {
            if (votes.get(leftIndex).avgVotes > pivot.avgVotes && votes.get(rightIndex).avgVotes < pivot.avgVotes) {
                QueryFunctions.AVGVotesByYear temp = votes.get(leftIndex);
                votes.set(leftIndex, votes.get(rightIndex));
                votes.set(rightIndex, temp);
            }

            if (votes.get(leftIndex).avgVotes <= pivot.avgVotes) {
                leftIndex++;
            }

            if (votes.get(rightIndex).avgVotes >= pivot.avgVotes) {
                rightIndex--;
            }
        }

        votes.set(right, votes.get(leftIndex));
        votes.set(leftIndex, pivot);
        return leftIndex;
    }

    // QuickSort Algorithm for 'GET_TOP_N_YEARS_BEST_AVG_VOTES'
    private static ArrayList<QueryFunctions.AVGVotesByYear> quickSortByAVGVotes(
            ArrayList<QueryFunctions.AVGVotesByYear> votes, int left, int right
    ) {
        if (left < right) {
            int pivotPos = paritionByAVGVotes(votes, left, right - 1);

            votes = quickSortByAVGVotes(votes, left, pivotPos);
            votes = quickSortByAVGVotes(votes, pivotPos + 1, right);
        }
        return votes;
    }

    // QuickSort Algorithm for 'GET_TOP_N_YEARS_BEST_AVG_VOTES'
    public static void quickSortByAVGVotes(ArrayList<QueryFunctions.AVGVotesByYear> votes) {
        quickSortByAVGVotes(votes, 0, votes.size());
    }

    // QuickSort Partition Algorithm for 'GET_TOP_N_MOVIES_RATIO'
    private static int paritionByMovieRatio(ArrayList<QueryFunctions.MovieRatio> movies, int left, int right) {
        QueryFunctions.MovieRatio pivot = movies.get(right);
        int leftIndex = left;
        int rightIndex = right - 1;

        while (leftIndex <= rightIndex) {
            if (movies.get(leftIndex).ratio > pivot.ratio && movies.get(rightIndex).ratio < pivot.ratio) {
                QueryFunctions.MovieRatio temp = movies.get(leftIndex);
                movies.set(leftIndex, movies.get(rightIndex));
                movies.set(rightIndex, temp);
            }

            if (movies.get(leftIndex).ratio <= pivot.ratio) {
                leftIndex++;
            }

            if (movies.get(rightIndex).ratio >= pivot.ratio) {
                rightIndex--;
            }
        }

        movies.set(right, movies.get(leftIndex));
        movies.set(leftIndex, pivot);
        return leftIndex;
    }

    // QuickSort Algorithm for 'GET_TOP_N_MOVIES_RATIO'
    private static ArrayList<QueryFunctions.MovieRatio> quickSortByMovieRatio(
            ArrayList<QueryFunctions.MovieRatio> movies, int left, int right
    ) {
        if (left < right) {
            int pivotPos = paritionByMovieRatio(movies, left, right - 1);

            movies = quickSortByMovieRatio(movies, left, pivotPos);
            movies = quickSortByMovieRatio(movies, pivotPos + 1, right);
        }
        return movies;
    }

    // QuickSort Algorithm for 'GET_TOP_N_MOVIES_RATIO'
    public static void quickSortByMovieRatio(ArrayList<QueryFunctions.MovieRatio> movies) {
        quickSortByMovieRatio(movies, 0, movies.size());
    }
}
