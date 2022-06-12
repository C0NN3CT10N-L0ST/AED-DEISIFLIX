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
}
