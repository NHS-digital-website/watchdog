package uk.nhs.england.sitemaps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.nhs.england.utils.Utils;
import uk.nhs.england.sitemaps.exceptions.NotValidRobotsTxtException;
import uk.nhs.england.sitemaps.models.UserAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RobotsScraper {

    private static final Logger logger = LogManager.getLogger(RobotsScraper.class);

    private final List<UserAgent> userAgents = new ArrayList<>();

    /**
     * @param robotsUrl The URL of the robots.txt file
     */
    public RobotsScraper(final String robotsUrl) throws IOException, NotValidRobotsTxtException {

        logger.debug("Fetching Robot URL " + robotsUrl);
        HttpURLConnection connection = Utils.openDecoratedConnection(new URL(robotsUrl));
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        logger.debug("Parsing Robot URL " + robotsUrl);
        int index = 0;

        while (reader.ready()) {
            String line = reader.readLine();

            if (!isLineAValidRobot(line)) {
                throwException();
            }

            if (index++ == 0) {
                if (isUserAgent(line)) {
                    addUserAgent(line);
                } else {
                    throwException();
                }
            } else {
                if (isUserAgent(line)) {
                    addUserAgent(line);
                } else if (isAllow(line)) {
                    getUserAgent().getAllowed().add(getValue(line));
                } else if (isDisallow(line)) {
                    getUserAgent().getDisallowed().add(getValue(line));
                } else if (isSitemap(line)) {
                    getUserAgent().getSitemaps().add(getValue(line));
                }
            }
        }
    }

    /**
     * @param line The line to parse
     * @return The value of the line
     */
    private static String getValue(String line) {
        String[] parts = line.toLowerCase().split(":");
        return String.join(":", Arrays.copyOfRange(parts, 1, parts.length)).trim();
    }

    private UserAgent getUserAgent() {
        return userAgents.get(userAgents.size() - 1);
    }

    private void addUserAgent(String line) {
        userAgents.add(new UserAgent(getValue(line)));
    }

    private boolean isLineAValidRobot(String line) {
        return isUserAgent(line) || isDisallow(line) || isAllow(line) || isSitemap(line);
    }

    private boolean isUserAgent(String line) {
        return line.toLowerCase().startsWith("user-agent:") && !getValue(line).isEmpty();
    }

    private boolean isAllow(String line) {
        return line.toLowerCase().startsWith("allow:") && !getValue(line).isEmpty();
    }

    private boolean isDisallow(String line) {
        return line.toLowerCase().startsWith("disallow:") && !getValue(line).isEmpty();
    }

    private boolean isSitemap(String line) {
        return line.toLowerCase().startsWith("sitemap:") && !getValue(line).isEmpty();
    }

    private void throwException() throws NotValidRobotsTxtException {
        throw new NotValidRobotsTxtException("The robots.txt file is not valid");
    }

    public List<UserAgent> getUserAgents() {
        return Collections.unmodifiableList(userAgents);
    }
}
