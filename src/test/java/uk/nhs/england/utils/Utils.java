package uk.nhs.england.utils;

import uk.nhs.england.utils.helpers.SystemProperties;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

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

    /**
     * A helper method that returns the correct HTTP or HTTPS connection respectfully, that has been decorated with
     * constant headers and/or properties.
     *
     * @param url The URL to open a connection to.
     * @return An HTTP or HTTPS connection.
     * @throws IllegalArgumentException If the URL protocol is not HTTP or HTTPS.
     * @throws IOException If there is a general I/O failure or no network connection.
     */
    public static HttpURLConnection openDecoratedConnection(URL url) throws IllegalArgumentException, IOException {
        return decorateConnection(openConnection(url));
    }

    /**
     * A helper method that decorates a HttpURLConnection with the correct headers and/or properties and returns it.
     *
     * @param connection The connection to decorate.
     * @return The decorated connection.
     */
    public static HttpURLConnection decorateConnection(HttpURLConnection connection) {
        // Set the user agent to mimic a web browser request
        connection.setRequestProperty("User-Agent", SystemProperties.getUserAgent());

        return connection;
    }

    /**
     * A helper method that checks if a string starts with a protocol (i.e. http or https) and has a domain
     * authority (i.e. example.com or example.com:8080).
     *
     * @param url The URL to check.
     * @return True if the URL has a protocol and domain authority, false otherwise.
     */
    public static boolean hasAProtocolAndAuthority(String url) {
        String regex = "^https?://[-a-zA-Z0-9.]+(:[0-9]+)?";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    /**
     * A helper method that returns the qualified location of a connection.
     *
     * @param connection
     * @return
     */
    public static String qualifiedLocation(HttpURLConnection connection) {
        String locationHeader = connection.getHeaderField("Location");
        if (locationHeader == null) {
            throw new IllegalArgumentException("The connection does not have a location header");
        }
        try {
            URL currentUrl = connection.getURL();
            URL locationUrl = new URL(currentUrl, locationHeader); // Handles both absolute and relative URLs

            return locationUrl.toString();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("The location header is not a valid URL, or URL path");
        }
    }

}
