package uk.nhs.england.sitemaps;

import uk.nhs.england.sitemaps.exceptions.NotValidRobotsTxtException;
import uk.nhs.england.sitemaps.models.UserAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.net.ssl.HttpsURLConnection;

public class RobotsScraper {

    private static final Logger logger = LogManager.getLogger(RobotsScraper.class);

    private List<UserAgent> userAgents = new ArrayList<>();


    /**
     * Fetches the robots.txt file from the given URL and parses it to find the sitemap URLs.
     *
     * @param robotsUrl The URL of the robots.txt file
     */
    public RobotsScraper(final String robotsUrl) throws IOException, NotValidRobotsTxtException {

        // Step 1: Fetch the robots.txt file
        logger.debug("Fetching " + robotsUrl);
        URL url = new URL(robotsUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        // Step 2: Parse the robots.txt
        logger.debug("Parsing " + robotsUrl);
        int index = 0; // Track the line number.

        while (reader.ready()) {
            String line = reader.readLine();

            if (!isLineAValidRobot(line)) {
                throwException();
            }

            if(index++ == 0) { // First line
                if(isUserAgent(line)) {
                    addUserAgent(line);
                } else {
                    throwException();
                }
            } else {
                if(isUserAgent(line)) { // All other lines
                    addUserAgent(line);
                } else if(isAllow(line)) {
                    getUserAgent().getAllowed().add(getValue(line));
                } else if(isDisallow(line)) {
                    getUserAgent().getDisallowed().add(getValue(line));
                } else if(isSitemap(line)) {
                    getUserAgent().getSitemaps().add(getValue(line));
                }
            }
        }
    }

    private UserAgent getUserAgent() {
        return userAgents.get(userAgents.size() -1);
    }

    private void addUserAgent(String line) {
        userAgents.add(new UserAgent(getValue(line)));
    }

    private boolean isLineAValidRobot(String line) {
        return isUserAgent(line) || isDisallow(line) || isAllow(line) || isSitemap(line);
    }

    private boolean isUserAgent(String line) {
        return line.toLowerCase().startsWith("user-agent:") && getValue(line).length() > 0;
    }
    
    private boolean isAllow(String line) {
        return line.toLowerCase().startsWith("allow:") && getValue(line).length() > 0;
    }
    
    private boolean isDisallow(String line) {
        return line.toLowerCase().startsWith("disallow:") && getValue(line).length() > 0;
    }

    private boolean isSitemap(String line) {
        return line.toLowerCase().startsWith("sitemap:") && getValue(line).length() > 0;
    }

    /**
     * Returns the value side of the given line with the key removed.
     *
     * @param line The line to parse
     * @return The value of the line
     */
    private static String getValue(String line) {
        String[] parts = line.toLowerCase().split(":");
        return String.join(":", Arrays.copyOfRange(parts, 1, parts.length)).trim();
    }

    private void throwException() throws NotValidRobotsTxtException {
        throw new NotValidRobotsTxtException("The robots.txt file is not valid");
    }

    public List<UserAgent> getUserAgents() {
        return Collections.unmodifiableList(userAgents);
    }
}
