package pt.ulusofona.deisi.aed.deisiflix;

import java.awt.image.AreaAveragingScaleFilter;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class ReaderFunctions {
    /**
     * Adds 'Pessoa' object to the correspondent movie field (based on the person type) in the movies array.
     * @param person Pessoa object to be added
     * @param personType Person type
     * @param movieID ID of the movie that were the object will be added
     * @param movies Array with all movies (sorted by ID)
     */
    public static void addPersonToMovies(Pessoa person, String personType, int movieID, HashMap<Integer, Filme> movies) {
        // Gets 'Filme' object for movie with 'movieID' ID
        Filme movie = movies.get(movieID);

        // Checks if 'movie' exists
        if (movie != null) {
            // Adds 'ACTOR'
            if (personType.equals("ACTOR")) {
                // Checks if 'atores' is null, if it is, creates a new 'ArrayList'
                if (movie.atores == null) {
                    movie.atores = new ArrayList<>();
                }

                // Adds person to 'atores'
                movie.atores.add(person);
            } else {  // ADDS 'DIRECTOR'
                // Checks if 'realizadores' is null, if it is, creates a new 'ArrayList'
                if (movie.realizadores == null) {
                    movie.realizadores = new ArrayList<>();
                }

                // Adds person to 'realizadores'
                movie.realizadores.add(person);
            }
        }
    }

    public static void addMovieToDuplicateLinesByYear(
            int lineNum,
            int movieID,
            int personID,
            HashMap<Integer, ArrayList<String>> duplicateLinesByYear,
            HashMap<Integer, Filme> moviesDict
    ) {
        // Gets 'Filme' object for 'movieID' movie
        Filme movie = moviesDict.get(movieID);

        // Checks if 'movie' exists
        if (movie != null) {
            // Movie date format
            DateTimeFormatter dateFileFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            // Gets year of the correspondent 'movieID'
            int year = LocalDate.parse(movie.dataLancamento, dateFileFormat).getYear();

            // Builds new string entry with the right format
            StringBuilder entry = new StringBuilder();
            entry.append(lineNum);
            entry.append(':');
            entry.append(personID);
            entry.append(movieID);

            // Adds duplicate person to 'duplicateLinesByYear' dict
            if (duplicateLinesByYear.containsKey(year)) {
                // Updates current entry
                duplicateLinesByYear.get(year).add(entry.toString());
            } else {
                // Creates new entry and adds the new string
                ArrayList<String> lines = new ArrayList<>();
                lines.add(entry.toString());
                duplicateLinesByYear.put(year, lines);
            }
        }
    }
}
