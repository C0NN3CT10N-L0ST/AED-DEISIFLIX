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
     * @param movies HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     */
    public static void addPersonToMovies(
            Pessoa person, String personType, int movieID, HashMap<Integer, Filme> movies
    ) {
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
     * Adds 'MovieAssociate' object to an HashMap
     * (KEY: MovieAssociate Name,VALUE: ArrayList of MovieAssociate's
     * in which the key corresponds to the name property of the 'MovieAssociate' object)
     * @param person The given 'MovieAssociate' object
     * @param people HashMap (KEY: Person Name, VALUE: ArrayList with all the people with the same name as the key)
     * @param lineNum The line number in which the given person entry is in the original file
     * @param duplicateLinesByYear HashMap (KEY: Year, VALUE: ArrayList with all the duplicate lines
     *                             in the following format '<lineNum>:<personID>:<movieID>')
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all movies
     */
    public static void addMovieAssociateToPeople(
            MovieAssociate person,
            HashMap<String, ArrayList<MovieAssociate>> people,
            int lineNum,
            HashMap<Integer, ArrayList<String>> duplicateLinesByYear,
            HashMap<Integer, Filme> moviesDict
    ) {
        // Checks if an entry for the person name already exists
        if (people.containsKey(person.name)) {
            // Checks if 'person' is already present in the 'ArrayList'
            // (returns -1 if it isn't present, the index in the ArrayList if it is)
            int personIndex = -1;
            for (int i = 0; i < people.get(person.name).size(); i++) {
                if (people.get(person.name).get(i).id == person.id) {
                    personIndex = i;
                }
            }

            // If person does not exists, adds it to the 'ArrayList'
            if (personIndex == -1) {
                people.get(person.name).add(person);
            } else {
                // If the person does exist, just adds the current movie to it in case it is not there yet

                // Gets movieID to be added to the current 'person' 'associatedMovieID'
                int movieID = person.associatedMoviesID.get(0);

                // Adds the movie to movieIDs list in case it does not yet exist
                if (people.get(person.name).get(personIndex).associatedMoviesID.contains(movieID)) {
                    addLineToDuplicateLinesByYear(lineNum, movieID, person.id, duplicateLinesByYear, moviesDict);
                } else {
                    people.get(person.name).get(personIndex).associatedMoviesID.add(movieID);
                }
            }
        } else {
            // If the 'HashMap' does not yet contain the key, create a new one and add the current 'person' object
            ArrayList<MovieAssociate> peopleWithSameName = new ArrayList<>();
            peopleWithSameName.add(person);

            // Creates new entry with 'person' name
            people.put(person.name, peopleWithSameName);
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
