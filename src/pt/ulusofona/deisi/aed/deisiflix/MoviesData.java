package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;

public class MoviesData {
    ArrayList<Filme> moviesFileOrder;
    Filme[] sortedMovies;
    ArrayList<String> ignoredLines;

    MoviesData() {}

    MoviesData(ArrayList<Filme> moviesFileOrder, Filme[] sortedMovies, ArrayList<String> ignoredLines) {
        this.moviesFileOrder = moviesFileOrder;
        this.sortedMovies = sortedMovies;
        this.ignoredLines = ignoredLines;
    }
}
