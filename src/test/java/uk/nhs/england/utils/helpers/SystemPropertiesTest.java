package uk.nhs.england.utils.helpers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.nhs.england.tags.Watchdog;

import static org.junit.jupiter.api.Assertions.*;

public class SystemPropertiesTest {

    @Test @Watchdog
    public void testGetDomainThrowsExceptWhenPropertyNotSet() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, SystemProperties::getDomain);
        assertEquals("DOMAIN property is not set correctly.", runtimeException.getMessage());
    }

    @Test @Watchdog
    public void testGetUsernameThrowsExceptWhenPropertyNotSet() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, SystemProperties::getUsername);
        assertEquals("USERNAME property is not set correctly.", runtimeException.getMessage());
    }

    @Test @Watchdog
    public void testGetPasswordThrowsExceptWhenPropertyNotSet() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, SystemProperties::getPassword);
        assertEquals("PASSWORD property is not set correctly.", runtimeException.getMessage());
    }

    @Test @Watchdog
    public void testGetDefaultPrototypalWhenPropertyNotSet() {
        assertEquals("https", SystemProperties.getProtocol());
    }

    @Test @Watchdog
    public void testGetNullForAuthTypeWhenPropertyNotSet() {
        assertNull(SystemProperties.getAuthType());
    }
}
