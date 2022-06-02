package pt.ulusofona.deisi.aed.deisiflix;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class AuxiliaryQueryFunctions {
    /**
     * Returns all the movies in which the year matches the given one.
     * @param year The given year
     * @param dateFormat Date format of the 'Filme' release date
     * @param movies List of movies to check
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all existing movies
     * @return Returns an 'ArrayList' with all the movies in which the year matches the given one
     */
    public static ArrayList<QueryFunctions.MovieActorYear> getMoviesFromYear(
            int year,
            DateTimeFormatter dateFormat,
            ArrayList<Integer> movies,
            HashMap<Integer, Filme> moviesDict
    ) {
        ArrayList<QueryFunctions.MovieActorYear> result = new ArrayList<>();

        for (Integer movieID : movies) {
            // Gets current 'Filme' object being checked
            Filme movie = moviesDict.get(movieID);
            if (movie != null) {
                LocalDate movieDate = LocalDate.parse(movie.dataLancamento, dateFormat);
                int movieYear = movieDate.getYear();

                // Adds 'movie' to 'moviesActorYear' in case the 'year' matches
                if (movieYear == year) {
                    String title = movie.titulo;
                    result.add(new QueryFunctions.MovieActorYear(title, movieDate));
                }
            }
        }
        return result;
    }

    /**
     * Stores all actors from all movies from a particular year in an HashSet.
     * @param year The given year
     * @param moviesByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @param actors HashSet to store the actor names
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all existing movies
     */
    public static void getUniqueMovieActors(int year, HashMap<Integer, ArrayList<Integer>> moviesByYear, HashSet<String> actors, HashMap<Integer, Filme> moviesDict) {
        // Gets all movie IDs from given year
        ArrayList<Integer> movies = moviesByYear.get(year);

        // Adds every actor of every movie to 'actors' HashSet
        for (int movieID : movies) {
            // Gets 'Filme' object
            Filme movie = moviesDict.get(movieID);

            // First it checks if 'movie' exists in 'moviesDict' and if 'atores' is not null
            if (movie != null && movie.atores != null) {
                for (Pessoa actor : movie.atores) {
                    actors.add(actor.nome);
                }
            }
        }
    }

    /**
     * Returns whether an ArrayList contains all actor names provided.
     * @param actors ArrayList where all actors are stored
     * @param actorNames String with all actor names separated by semicolons
     * @return Returns whether all actors are contained in the ArrayList
     */
    public static boolean containsActors(ArrayList<Pessoa> actors, String actorNames) {
        // Gets names from 'actorNames' (String with names separated by semicolons)
        String[] names = actorNames.split(";");
        // Gets the number of actors
        int actorsNum = names.length;
        // Counts the number of actor names in 'actors'
        int actorsCount = 0;

        // Iterate over the ArrayList
        for (Pessoa actor : actors) {
            for (String name : names) {
                if (Objects.equals(actor.nome, name)) {
                    actorsCount++;
                }
            }
        }
        // If 'actorsCount' matches 'actorsNum' all actors are in 'actors'
        return actorsCount == actorsNum;
    }

    /**
     * Calculates Gender Percentual Discrepancy for all movies in the given year.
     * @param year The given year
     * @param moviesByYear HashMap (KEY: year, VALUE: ArrayList with movie IDs) with all movies sorted by year
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all existing movies
     * @param moviesGenderBias ArrayList where all calculated value will be stored
     */
    public static void calculateGenderPercentualDiscrepancy(
            int year,
            HashMap<Integer, ArrayList<Integer>> moviesByYear,
            HashMap<Integer, Filme> moviesDict,
            ArrayList<QueryFunctions.MovieGenderBias> moviesGenderBias
    ) {
        // Iterates through every movie in the given 'year'
        for (Integer movieID : moviesByYear.get(year)) {
            // Gets 'Filme' object
            Filme movie = moviesDict.get(movieID);

            // Verifies that 'movie' exists in 'moviesDict' and that 'atores' is not null
            if (movie != null && movie.atores != null) {
                int totalActors = movie.atores.size();
                boolean hasEnoughPeople = totalActors >= 10;

                // Only does the calculation if movie has more than 10 actors
                if (hasEnoughPeople) {
                    int actorsNum = 0;
                    int actressesNum = 0;
                    int discrepancy;
                    char predominantGender;

                    // Iterates over all people in 'atores' for each movie and counts them based on gender
                    for (Pessoa person : movie.atores) {
                        if (person.genero == 'M') {
                            actorsNum++;
                        }

                        if (person.genero == 'F') {
                            actressesNum++;
                        }
                    }

                    // Checks which is the predominant gender and calculates percentual discrepancy based on that
                    if (actorsNum > actressesNum) {
                        discrepancy = actorsNum * 100 / totalActors;
                        predominantGender = 'M';
                    } else {
                        discrepancy = actressesNum * 100 / totalActors;
                        predominantGender = 'F';
                    }

                    // Adds movie gender data to 'moviesGenderBias'
                    moviesGenderBias.add(new QueryFunctions.MovieGenderBias(movie.titulo, discrepancy, predominantGender));
                }
            }
        }
    }

    /**
     * Calculates the movie average votes from all movies for the given year.
     * @param movieIDs ArrayList that contains all the movie IDs for the given year
     * @param moviesDict HashMap (KEY: movie ID, VALUE: 'Filme' object) with all existing movies
     * @return Returns movies' average votes for the given year
     */
    public static float calculateYearVotesAverage(ArrayList<Integer> movieIDs, HashMap<Integer, Filme> moviesDict) {
        float totalVotesAverage = 0.0F;

        // Adds each movie votesAverage to 'totalVotesAverage'
        for (Integer movieID : movieIDs) {
            // Gets movie position in 'sortedMovies'
            Filme movie = moviesDict.get(movieID);

            if (movie != null) {
                totalVotesAverage += movie.mediaVotos;
            }
        }
        return totalVotesAverage / movieIDs.size();
    }
}
