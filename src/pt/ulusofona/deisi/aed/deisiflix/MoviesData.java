package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;
import java.util.HashMap;

public class MoviesData {
    ArrayList<Filme> moviesFileOrder;
    Filme[] sortedMoviesByID;
    ArrayList<String> ignoredLines;
    HashMap<Integer, ArrayList<Integer>> movieIDsByYear;

    MoviesData() {}

    MoviesData(ArrayList<Filme> moviesFileOrder,
               Filme[] sortedMoviesByID,
               HashMap<Integer, ArrayList<Integer>> movieIDsByYear,
               ArrayList<String> ignoredLines) {
        this.moviesFileOrder = moviesFileOrder;
        this.sortedMoviesByID = sortedMoviesByID;
        this.movieIDsByYear = movieIDsByYear;
        this.ignoredLines = ignoredLines;
    }
}
