package pt.ulusofona.deisi.aed.deisiflix;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestFilme {
    @Test
    public void testToStringNormal() {
        Filme movie = new Filme(33,
                "Interstellar",
                null,
                null,
                null,
                "07-11-2014",
                165000000,
                8.6f,
                1718186
        );

        String realResult = movie.toString();
        String expectedResult = "33 | Interstellar | 2014-11-07 | 1718186 | 8.6";

        assertEquals(expectedResult, realResult);
    }

    @Test
    public void testToStringWithNullValues() {
        Filme movie = new Filme(33,
                "",
                null,
                null,
                null,
                "07-11-2014",
                165000000,
                8.6f,
                1718186
        );

        String realResult = movie.toString();
        String expectedResult = "33 |  | 2014-11-07 | 1718186 | 8.6";

        assertEquals("Object 'Filme' with empty title", expectedResult, realResult);
    }
}
