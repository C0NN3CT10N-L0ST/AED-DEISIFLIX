import java.util.ArrayList;

public class MoviesData {
    ArrayList<Filme> movies;
    ArrayList<String> ignoredLines;

    MoviesData() {}

    MoviesData(ArrayList<Filme> movies, ArrayList<String> ignoredLines) {
        this.movies = movies;
        this.ignoredLines = ignoredLines;
    }
}
