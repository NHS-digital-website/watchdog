package uk.nhs.england;

import org.junit.jupiter.api.Test;
import uk.nhs.england.tags.Production;
import uk.nhs.england.tags.Uat;
import uk.nhs.england.tags.Watchdog;
import uk.nhs.england.utils.helpers.BasicAuthenticator;
import uk.nhs.england.utils.helpers.SystemProperties;

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
        assertNotNull(SystemProperties.getDomain());
        if(BasicAuthenticator.usingBasicAuth()) {
            assertNotNull(SystemProperties.getUsername());
            assertNotNull(SystemProperties.getPassword());
        }
    }

    @Test @Production
    public void productionOnly() {
        assertNotNull(SystemProperties.getDomain());
    }

    @Test
    public void anySubjectsExceptWatchdogItself() {
        assertNotNull(SystemProperties.getDomain());
    }

    @Test @Watchdog
    public void watchdogOnly() {
        assertTrue(true);
    }

}

