package pt.ulusofona.deisi.aed.deisiflix;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestFilme {
    @Test
    public void testToStringNormal() {
        ArrayList<GeneroCinematografico> genres = new ArrayList<>();
        genres.add(new GeneroCinematografico("Adventure"));
        genres.add(new GeneroCinematografico("Drama"));
        genres.add(new GeneroCinematografico("Sci-Fi"));

        ArrayList<Pessoa> actors = new ArrayList<>();
        actors.add(new Pessoa(432, "Matthew McConaughey", 'M'));
        actors.add(new Pessoa(412, "Anne Hathaway", 'F'));
        actors.add(new Pessoa(989, "Jessica Chastain", 'F'));

        ArrayList<Pessoa> directors = new ArrayList<>();
        directors.add(new Pessoa(897, "Christopher Nolan", 'M'));

        Filme movie = new Filme(
                33,
                "Interstellar",
                actors,
                directors,
                genres,
                "07-11-2014",
                165000000,
                8.6,
                1718186
        );

        String realResult = movie.toString();
        String expectedResult = "33 | Interstellar | 2014-11-07 | 1718186 | 8.6 | 3 | 1 | 1 | 2";

        assertEquals(expectedResult, realResult);
    }

    @Test
    public void testToStringWithEmptyValues() {
        Filme movie = new Filme(
                33,
                "",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                "07-11-2014",
                165000000,
                8.6,
                1718186
        );

        String realResult = movie.toString();
        String expectedResult = "33 |  | 2014-11-07 | 1718186 | 8.6 | 0 | 0 | 0 | 0";

        assertEquals("Object 'Filme' with empty title", expectedResult, realResult);
    }
}
