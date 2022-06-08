package pt.ulusofona.deisi.aed.deisiflix;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class PeopleData {
    // KEY: Person Name, VALUE: ArrayList with 'MovieAssociate' instances for each actor with the same name
    HashMap<String, ArrayList<MovieAssociate>> moviesPeople;
    // All actor sorted By ID (KEY: Actor ID, VALUE: Actor Name)
    HashMap<Integer, String> actorsByID;
    // All ingnored lines from 'deisi_people.txt' untouched
    ArrayList<String> ignoredLines;
    // KEY: Year, VALUE: ArrayList ordered by line number with the format '<lineNum>:<personID>:<movieID>'
    HashMap<Integer, ArrayList<String>> duplicateLinesYear;

    PeopleData() {}

    PeopleData(
            HashMap<String, ArrayList<MovieAssociate>> moviesPeople,
            HashMap<Integer, String> actorsByID,
            HashMap<Integer, ArrayList<String>> duplicateLinesYear,
            ArrayList<String> ignoredLines
    ) {
        this.moviesPeople = moviesPeople;
        this.actorsByID = actorsByID;
        this.duplicateLinesYear = duplicateLinesYear;
        this.ignoredLines = ignoredLines;
    }
}
