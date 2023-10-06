package uk.nhs.england;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {

    private static final Logger logger = LogManager.getLogger(Utils.class);

    public static String getDomain() {
        String domain = System.getProperty("domain");
        if (domain == null) {
            throw new RuntimeException("domain property is not set");
        } else {
            logger.info("Domain property is set to " + domain);
        }
        return domain;
    }

    public static String getProtocol() {
        return System.getProperty("protocol", "https");
    }

    /**
     * A helper method that returns the correct HTTP or HTTPS connection respectfully.
     *
     * @param url The URL to open a connection to.
     * @return An HTTP or HTTPS connection.
     * @throws IllegalArgumentException If the URL protocol is not HTTP or HTTPS.
     * @throws IOException If there is a general I/O failure or no network connection.
     */
    public static HttpURLConnection openConnection(URL url) throws IllegalArgumentException, IOException {
        if (url.getProtocol().equalsIgnoreCase("https")) {
            return (HttpsURLConnection) url.openConnection();
        } else if (url.getProtocol().equalsIgnoreCase("http")) {
            return (HttpURLConnection) url.openConnection();
        } else {
            throw new IllegalArgumentException("The URL protocol must be HTTP or HTTPS");
        }
    }
}
