package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;
import java.util.HashMap;

public class MoviesData {
    ArrayList<Filme> moviesFileOrder;
    HashMap<Integer, Filme> moviesDict;
    ArrayList<String> ignoredLines;
    HashMap<Integer, ArrayList<Integer>> movieIDsByYear;

    MoviesData() {}

    MoviesData(ArrayList<Filme> moviesFileOrder,
               HashMap<Integer, Filme> moviesDict,
               HashMap<Integer, ArrayList<Integer>> movieIDsByYear,
               ArrayList<String> ignoredLines) {
        this.moviesFileOrder = moviesFileOrder;
        this.moviesDict = moviesDict;
        this.movieIDsByYear = movieIDsByYear;
        this.ignoredLines = ignoredLines;
    }
}
