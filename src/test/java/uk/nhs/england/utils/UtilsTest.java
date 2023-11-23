package uk.nhs.england.utils;

import org.junit.jupiter.api.Test;
import uk.nhs.england.tags.Watchdog;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test @Watchdog
    public void testConnectionType() throws Exception {
        HttpURLConnection https = Utils.openDecoratedConnection(new URL("https://www.google.com"));
        assertEquals("class sun.net.www.protocol.https.HttpsURLConnectionImpl", https.getClass().toString());
        https.disconnect();

        HttpURLConnection http = Utils.openDecoratedConnection(new URL("http://www.google.com"));
        assertEquals("class sun.net.www.protocol.http.HttpURLConnection", http.getClass().toString());
        http.disconnect();
    }

    @Test @Watchdog
    public void testHasAPrototypicalAndAuthority() {
        String[] testUrls = {
                "https://example.com/",
                "http://example.com:8080/",
                "http://localhost/",
                "http://localhost:3000/",
                "http://localhost/path/to/resource",
                "http://localhost:3000/path/to/resource"
        };
        for (String url : testUrls) {
            assertTrue(Utils.hasAProtocolAndAuthority(url));
        }

    }

}