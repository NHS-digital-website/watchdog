package uk.nhs.england;

import org.junit.jupiter.api.Test;
import uk.nhs.england.tags.Production;
import uk.nhs.england.tags.Uat;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class HelloWorldTest {

    @Test @Uat
    public void productionShouldAnswerWithTrue() {
        assertTrue(false);
    }

    @Test @Production
    public void uatShouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void alwaysShouldAnswerWithTrue() {
        assertTrue(true);
    }

}
