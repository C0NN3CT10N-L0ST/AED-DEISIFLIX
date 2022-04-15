import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static void lerFicheiros() throws IOException {
        Reader.movieReader();
        Reader.movieVotesReader();
        Reader.genresReader();
        Reader.peopleReader();
    }

    static ArrayList<Filme> getFilmes() throws IOException {
        return Reader.movieReader();
    }

    static ArrayList<String> getLinhasIgnoradas(String fileName) throws IOException {
        switch (fileName) {
            case "deisi_movies.txt":
            case "deisi_people.txt":
                return Reader.getIgnoredLines(fileName, 5);
            case "deisi_movie_votes.txt":
                return Reader.getIgnoredLines(fileName, 3);
            case "deisi_genres.txt":
                return Reader.getIgnoredLines(fileName, 2);
            default:
                return null;
        }
    }

    public static void main(String[] args) throws IOException {
        Reader.movieReader();
    }
}
