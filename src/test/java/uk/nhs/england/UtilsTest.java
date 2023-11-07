package uk.nhs.england;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import uk.nhs.england.tags.Watchdog;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    private static final Logger logger = LogManager.getLogger(UtilsTest.class);

    @Test @Watchdog
    public void testGetDomainThrowsExceptWhenPropertyNotSet() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, Utils::getDomain);
        assertEquals("domain property is not set", runtimeException.getMessage());
    }

    @Test @Watchdog
    public void testConnectionType() throws Exception {
        HttpURLConnection https = Utils.openConnection(new URL("https://www.google.com"));
        assertEquals("class sun.net.www.protocol.https.HttpsURLConnectionImpl", https.getClass().toString());
        https.disconnect();

        HttpURLConnection http = Utils.openConnection(new URL("http://www.google.com"));
        assertEquals("class sun.net.www.protocol.http.HttpURLConnection", http.getClass().toString());
        http.disconnect();
    }

}