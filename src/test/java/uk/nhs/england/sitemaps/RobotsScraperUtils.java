package uk.nhs.england.sitemaps;

import uk.nhs.england.utils.helpers.SystemProperties;
import uk.nhs.england.sitemaps.exceptions.NotValidRobotsTxtException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RobotsScraperUtils {

    private static final RobotsScraper robotsScraper;

    static {
        try {
            robotsScraper = new RobotsScraper(SystemProperties.getProtocol() + "://" + SystemProperties.getDomain() + "/robots.txt");
        } catch (IOException | NotValidRobotsTxtException e) {
            throw new RuntimeException("Error initializing RobotsScraper", e);
        }
    }

    private RobotsScraperUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * @return A list of sitemap URLs found in the robots.txt file.
     */
    static List<String> getSitemapsFromRobotsTxt() {
        return robotsScraper.getUserAgents().stream().flatMap(userAgent -> userAgent.getSitemaps().stream())
                .collect(Collectors.toList());
    }

}
