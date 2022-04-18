package pt.ulusofona.deisi.aed.deisiflix;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    // Global var
    static ArrayList<Filme> movies = new ArrayList<Filme>();
    static ArrayList<String> moviesIgnoredLines = new ArrayList<String>();
    static ArrayList<String> votesIgnoredLines = new ArrayList<String>();
    static ArrayList<String> peopleIgnoredLines = new ArrayList<String>();
    static ArrayList<String> genresIgnoredLines = new ArrayList<String>();

    static void lerFicheiros() throws IOException {
        MoviesData moviesReader = Reader.movieReader();
        movies = moviesReader.movies;
        moviesIgnoredLines = moviesReader.ignoredLines;
        votesIgnoredLines = Reader.movieVotesReader(movies);
        peopleIgnoredLines = Reader.peopleReader();
        genresIgnoredLines = Reader.genresReader();
    }

    static ArrayList<Filme> getFilmes() {
        return movies;
    }

    static ArrayList<String> getLinhasIgnoradas(String fileName) {
        switch (fileName) {
            case "deisi_movies.txt":
                return moviesIgnoredLines;
            case "deisi_movie_votes.txt":
                return votesIgnoredLines;
            case "deisi_people.txt":
                return peopleIgnoredLines;
            case "deisi_genres.txt":
                return genresIgnoredLines;
            default:
                return null;
        }
    }

    public static void main(String[] args) throws IOException {
        // DEBUGGING
        // lerFicheiros();

        // for (int i = 0; i < movies.size(); i++) {
        //     System.out.println(movies.get(i).toString());
        // }
    }
}
