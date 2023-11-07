package uk.nhs.england;

import org.junit.jupiter.api.Test;
import uk.nhs.england.tags.Production;
import uk.nhs.england.tags.Uat;
import uk.nhs.england.tags.Watchdog;

import static org.junit.jupiter.api.Assertions.*;

/**
 * These tests help validate the run properties for a respective subject have been set correctly.
 *
 * For example, if targeting production: 'productionOnly' and 'anySubjectsExceptWatchdogItself' should run. But no other
 * tests in this should have run.
 */
public class CheckSubjectTest {

    @Test @Uat
    public void uatOnly() {
        assertTrue(true);
        assertNotNull(Utils.getDomain());
    }

    @Test @Production
    public void productionOnly() {
        assertTrue(true);
        assertNotNull(Utils.getDomain());
    }

    @Test
    public void anySubjectsExceptWatchdogItself() {
        assertTrue(true);
        assertNotNull(Utils.getDomain());
    }

    @Test @Watchdog
    public void watchdogOnly() {
        assertTrue(true);
    }

}

