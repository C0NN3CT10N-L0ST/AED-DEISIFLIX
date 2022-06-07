package pt.ulusofona.deisi.aed.deisiflix;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class PeopleData {
    // KEY: Person Name, VALUE: ArrayList with 'MovieAssociate' instances for each actor with the same name
    HashMap<String, ArrayList<MovieAssociate>> moviesPeople;
    // All ingnored lines from 'deisi_people.txt' untouched
    ArrayList<String> ignoredLines;
    // KEY: Year, VALUE: ArrayList ordered by line number with the format '<lineNum>:<personID>:<movieID>'
    HashMap<Integer, ArrayList<String>> duplicateLinesYear;

    PeopleData() {}

    PeopleData(
            HashMap<String, ArrayList<MovieAssociate>> moviesPeople,
            HashMap<Integer, ArrayList<String>> duplicateLinesYear,
            ArrayList<String> ignoredLines
    ) {
        this.moviesPeople = moviesPeople;
        this.duplicateLinesYear = duplicateLinesYear;
        this.ignoredLines = ignoredLines;
    }
}
