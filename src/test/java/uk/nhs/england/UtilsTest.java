package uk.nhs.england;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import uk.nhs.england.tags.Watchdog;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    private static final Logger logger = LogManager.getLogger(UtilsTest.class);

    @Test @Watchdog
    public void testGetDomainThrowsExceptWhenPropertyNotSet() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> Utils.getDomain());
        assertEquals("domain property is not set", runtimeException.getMessage());
    }

}