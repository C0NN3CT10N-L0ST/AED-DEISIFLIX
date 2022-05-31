package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;
import java.util.HashMap;

public class MoviesData {
    ArrayList<Filme> moviesFileOrder;
    Filme[] sortedMovies;
    ArrayList<String> ignoredLines;
    HashMap<Integer, ArrayList<Integer>> movieIDsByYear;

    MoviesData() {}

    MoviesData(ArrayList<Filme> moviesFileOrder,
               Filme[] sortedMovies,
               HashMap<Integer, ArrayList<Integer>> movieIDsByYear,
               ArrayList<String> ignoredLines) {
        this.moviesFileOrder = moviesFileOrder;
        this.sortedMovies = sortedMovies;
        this.movieIDsByYear = movieIDsByYear;
        this.ignoredLines = ignoredLines;
    }
}
