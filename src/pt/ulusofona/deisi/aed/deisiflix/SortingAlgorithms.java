package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;

public class SortingAlgorithms {
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

    // QuickSort Partition Algorithm for 'TOP_6_DIRECTORS_WITHIN_FAMILY'
    private static int paritionByFamilyDirections(
            ArrayList<QueryFunctions.DirectorsFamily> directions, int left, int right
    ) {
        QueryFunctions.DirectorsFamily pivot = directions.get(right);
        int leftIndex = left;
        int rightIndex = right - 1;

        while (leftIndex <= rightIndex) {
            if (directions.get(leftIndex).familyDirections > pivot.familyDirections
                    && directions.get(rightIndex).familyDirections < pivot.familyDirections) {
                QueryFunctions.DirectorsFamily temp = directions.get(leftIndex);
                directions.set(leftIndex, directions.get(rightIndex));
                directions.set(rightIndex, temp);
            }

            if (directions.get(leftIndex).familyDirections <= pivot.familyDirections) {
                leftIndex++;
            }

            if (directions.get(rightIndex).familyDirections >= pivot.familyDirections) {
                rightIndex--;
            }
        }

        directions.set(right, directions.get(leftIndex));
        directions.set(leftIndex, pivot);
        return leftIndex;
    }

    // QuickSort Algorithm for 'TOP_6_DIRECTORS_WITHIN_FAMILY'
    private static ArrayList<QueryFunctions.DirectorsFamily> quickSortByFamilyDirections(
            ArrayList<QueryFunctions.DirectorsFamily> directions, int left, int right
    ) {
        if (left < right) {
            int pivotPos = paritionByFamilyDirections(directions, left, right - 1);

            directions = quickSortByFamilyDirections(directions, left, pivotPos);
            directions = quickSortByFamilyDirections(directions, pivotPos + 1, right);
        }
        return directions;
    }

    // QuickSort Algorithm for 'TOP_6_DIRECTORS_WITHIN_FAMILY'
    public static void quickSortByFamilyDirections(ArrayList<QueryFunctions.DirectorsFamily> directions) {
        quickSortByFamilyDirections(directions, 0, directions.size());
    }

    // QuickSort Partition Algorithm for 'TOP_N_MOST_EXPENSIVE_MOVIES_YEAR'
    private static int paritionByMovieBudget(ArrayList<QueryFunctions.MovieBudget> movies, int left, int right) {
        QueryFunctions.MovieBudget pivot = movies.get(right);
        int leftIndex = left;
        int rightIndex = right - 1;

        while (leftIndex <= rightIndex) {
            if (movies.get(leftIndex).budget > pivot.budget && movies.get(rightIndex).budget < pivot.budget) {
                QueryFunctions.MovieBudget temp = movies.get(leftIndex);
                movies.set(leftIndex, movies.get(rightIndex));
                movies.set(rightIndex, temp);
            }

            if (movies.get(leftIndex).budget <= pivot.budget) {
                leftIndex++;
            }

            if (movies.get(rightIndex).budget >= pivot.budget) {
                rightIndex--;
            }
        }

        movies.set(right, movies.get(leftIndex));
        movies.set(leftIndex, pivot);
        return leftIndex;
    }

    // QuickSort Algorithm for 'TOP_N_MOST_EXPENSIVE_MOVIES_YEAR'
    private static ArrayList<QueryFunctions.MovieBudget> quickSortByMovieBudget(
            ArrayList<QueryFunctions.MovieBudget> movies, int left, int right
    ) {
        if (left < right) {
            int pivotPos = paritionByMovieBudget(movies, left, right - 1);

            movies = quickSortByMovieBudget(movies, left, pivotPos);
            movies = quickSortByMovieBudget(movies, pivotPos + 1, right);
        }
        return movies;
    }

    // QuickSort Algorithm for 'TOP_N_MOST_EXPENSIVE_MOVIES_YEAR'
    public static void quickSortByMovieBudget(ArrayList<QueryFunctions.MovieBudget> movies) {
        quickSortByMovieBudget(movies, 0, movies.size());
    }
}
