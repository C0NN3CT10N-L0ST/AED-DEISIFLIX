package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;

public class MovieAssociate {
    int id;
    String name;
    Character gender;
    String type;  // Type of associate ('ACTOR' or 'DIRECTOR')
    ArrayList<Integer> associatedMoviesID;  // Movies in which the person participated

    MovieAssociate() {}

    MovieAssociate(int id, String name, Character gender, String type, ArrayList<Integer> associatedMoviesID) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.type = type;
        this.associatedMoviesID = associatedMoviesID;
    }
}
