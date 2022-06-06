package pt.ulusofona.deisi.aed.deisiflix;

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

    /**
     * Adds a duplicate line from 'deisi_people.txt' file to an HashMap
     * which stores all the duplicate lines for that file.
     * @param lineNum The line number of the duplicate line in the original file
     * @param movieID The movie ID of the person movie
     * @param personID The person ID
     * @param duplicateLinesByYear HashMap (KEY: Year, VALUE: ArrayList with all the duplicate lines
     *                              in the following format '<lineNum>:<personID>:<movieID>')
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     */
    public static void addLineToDuplicateLinesByYear(
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
            entry.append(':');
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
