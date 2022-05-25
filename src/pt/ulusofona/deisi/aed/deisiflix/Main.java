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

    public static void lerFicheiros() throws IOException {
        MoviesData moviesReader = Reader.movieReader();
        movies = moviesReader.movies;
        moviesIgnoredLines = moviesReader.ignoredLines;
        votesIgnoredLines = Reader.movieVotesReader(movies);
        peopleIgnoredLines = Reader.peopleReader();
        genresIgnoredLines = Reader.genresReader();
    }

    public static ArrayList<Filme> getFilmes() {
        return movies;
    }

    public static ArrayList<String> getLinhasIgnoradas(String fileName) {
        return switch (fileName) {
            case "deisi_movies.txt" -> moviesIgnoredLines;
            case "deisi_movie_votes.txt" -> votesIgnoredLines;
            case "deisi_people.txt" -> peopleIgnoredLines;
            case "deisi_genres.txt" -> genresIgnoredLines;
            default -> null;
        };
    }

    public static QueryResult perguntar(String pergunta) {
        // TODO: Implement this
        return new QueryResult();
    }

    public static String getVideoURL() {
        // TODO: Add video here
        return "";
    }

    public static String getCreativeQuery() {
        // TODO: Implement creative query
        return "";
    }

    public static void main(String[] args) throws IOException {
        // DEBUGGING
        lerFicheiros();

        for (Filme movie : movies) {
            System.out.println(movie.toString());
        }
        System.out.println("Total filmes: " + movies.size());
        System.out.println("Total filmes ignorados: " + moviesIgnoredLines.size());
    }
}
