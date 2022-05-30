package pt.ulusofona.deisi.aed.deisiflix;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class PeopleData {
    HashMap<String, MovieAssociate> moviesPeople;
    ArrayList<String> ignoredLines;
    ArrayList<String> duplicateLinesYear;

    PeopleData() {}

    PeopleData(HashMap<String, MovieAssociate> moviesPeople, ArrayList<String> duplicateLinesYear, ArrayList<String> ignoredLines) {
        this.moviesPeople = moviesPeople;
        this.duplicateLinesYear = duplicateLinesYear;
        this.ignoredLines = ignoredLines;
    }
}
