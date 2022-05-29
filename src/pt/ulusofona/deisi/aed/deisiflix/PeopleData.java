package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;
import java.util.HashMap;

public class PeopleData {
    HashMap<String, MovieAssociate> moviesPeople;
    ArrayList<String> ignoredLines;

    PeopleData() {}

    PeopleData(HashMap<String, MovieAssociate> moviesPeople, ArrayList<String> ignoredLines) {
        this.moviesPeople = moviesPeople;
        this.ignoredLines = ignoredLines;
    }
}
