package pt.ulusofona.deisi.aed.deisiflix;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestQueryFunctions {
    @Test
    public void testCreativeQuery() throws IOException {
        Main.lerFicheirosTestes();

        ArrayList<String> expectedResult = new ArrayList<>(Arrays.asList(("The Hobbit: The Battle of the Five Armies - Budget: $250000000\n" +
                "X-Men: Days of Future Past - Budget: $250000000\n" +
                "Transformers: Age of Extinction - Budget: $210000000\n" +
                "The Amazing Spider-Man 2 - Budget: $200000000\n" +
                "Maleficent - Budget: $180000000\n" +
                "Edge of Tomorrow - Budget: $178000000\n" +
                "Guardians of the Galaxy - Budget: $170000000\n" +
                "Captain America: The Winter Soldier - Budget: $170000000\n" +
                "Dawn of the Planet of the Apes - Budget: $170000000\n" +
                "Interstellar - Budget: $165000000").split("\n")));

        ArrayList<String> realResult = new ArrayList<>(
                Arrays.asList(Main.perguntar("TOP_10_MOST_EXPENSIVE_MOVIES_YEAR 2014").valor.split("\n")));

        boolean resultIsCorrect = true;

        for (String line : realResult) {
            if (!expectedResult.contains(line)) {
                resultIsCorrect = false;
            }
        }

        expectedResult = new ArrayList<>(Arrays.asList(("Transformers: The Last Knight - Budget: $260000000\n" +
                "The Fate of the Furious - Budget: $250000000\n" +
                "Pirates of the Caribbean: Dead Men Tell No Tales - Budget: $230000000\n" +
                "Guardians of the Galaxy Vol. 2 - Budget: $200000000\n" +
                "Valerian and the City of a Thousand Planets - Budget: $197471676\n" +
                "Kong: Skull Island - Budget: $185000000\n" +
                "Cars 3 - Budget: $175000000\n" +
                "King Arthur: Legend of the Sword - Budget: $175000000\n" +
                "Beauty and the Beast - Budget: $160000000\n" +
                "War for the Planet of the Apes - Budget: $152000000").split("\n")));

        realResult = new ArrayList<>(
                Arrays.asList(Main.perguntar("TOP_10_MOST_EXPENSIVE_MOVIES_YEAR 2017").valor.split("\n")));

        resultIsCorrect = true;

        for (String line : realResult) {
            if (!expectedResult.contains(line)) {
                resultIsCorrect = false;
            }
        }

        assertTrue(resultIsCorrect);
    }

    @Test
    public void testCountMoviesActor() throws IOException {
        Main.lerFicheirosTestes();

        String expectedResult = "47";
        String realResult = Main.perguntar("COUNT_MOVIES_ACTOR Tom Cruise").valor;
        assertEquals(expectedResult, realResult);

        expectedResult = "53";
        realResult = Main.perguntar("COUNT_MOVIES_ACTOR Clark Gable").valor;
        assertEquals(expectedResult, realResult);
    }

    @Test
    public void testGetMoviesActorYear() throws IOException {
        Main.lerFicheirosTestes();

        String expectedResult = "Mission: Impossible III (2006-05-03)";
        String realResult = Main.perguntar("GET_MOVIES_ACTOR_YEAR Tom Cruise 2006").valor;
        assertEquals(expectedResult, realResult);

        expectedResult = "The Tall Men (1955-09-22)\nSoldier of Fortune (1955-01-01)";
        realResult = Main.perguntar("GET_MOVIES_ACTOR_YEAR Clark Gable 1955").valor;
        assertEquals(expectedResult, realResult);
    }

    @Test
    public void testCountMoviesWithActors() throws IOException {
        Main.lerFicheirosTestes();

        String expectedResult = "2";
        String realResult = Main.perguntar("COUNT_MOVIES_WITH_ACTORS Brad Pitt;Angelina Jolie").valor;
        assertEquals(expectedResult, realResult);

        expectedResult = "2";
        realResult = Main.perguntar("COUNT_MOVIES_WITH_ACTORS Tom Cruise;Philip Seymour Hoffman").valor;
        assertEquals(expectedResult, realResult);
    }

    @Test
    public void testCountActors3Years() throws IOException {
        Main.lerFicheirosTestes();

        String expectedResult = "48";
        String realResult = Main.perguntar("COUNT_ACTORS_3_YEARS 1970 2010 2015").valor;
        assertEquals(expectedResult, realResult);

        expectedResult = "944";
        realResult = Main.perguntar("COUNT_ACTORS_3_YEARS 2001 2010 2014").valor;
        assertEquals(expectedResult, realResult);
    }

    @Test
    public void testTopMoviesWithGenderBias() throws IOException {
        Main.lerFicheirosTestes();
        boolean resultIsCorrect = true;

        ArrayList<String> expectedResultFirstElementsAnyOrder = new ArrayList<>(
                Arrays.asList(("There Once Was a Singing Blackbird:F:100\n" +
                "The Man Who Left His Will on Film:F:100\n" +
                "The Wizard of Gore:F:100\n" +
                "Even Dwarfs Started Small:F:100\n" +
                "Portraits of Women:F:100").split("\n")));

        String expectedResultLastElement = "The Honeymoon Killers:F:94";

        ArrayList<String> realResult = new ArrayList<>(
                Arrays.asList(Main.perguntar("TOP_MOVIES_WITH_GENDER_BIAS 6 1970").valor.split("\n")));

        for (int i = 0; i < 6; i++) {
            if (i == 5) {
                if (!realResult.get(i).equals(expectedResultLastElement)) {
                    resultIsCorrect = false;
                }
            } else if (!expectedResultFirstElementsAnyOrder.contains(realResult.get(i))) {
                resultIsCorrect = false;
            }
        }
        assertTrue(resultIsCorrect);
    }

    /*@Test
    public void testGetRecentTitlesSameAVGVotesOneSharedActor() throws IOException {
        Main.lerFicheirosTestes();


    }*/
}
