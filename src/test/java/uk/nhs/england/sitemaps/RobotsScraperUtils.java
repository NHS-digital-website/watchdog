package uk.nhs.england.sitemaps;

import uk.nhs.england.Utils;
import uk.nhs.england.sitemaps.exceptions.NotValidRobotsTxtException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RobotsScraperUtils {

    private static final RobotsScraper robotsScraper;

    private RobotsScraperUtils() {
        throw new IllegalStateException("Utility class");
    }

    static {
        try {
            robotsScraper = new RobotsScraper("https://" + Utils.getDomain() + "/robots.txt");
        } catch (IOException | NotValidRobotsTxtException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a list of sitemap URLs from the robots.txt file.
     *
     * This call uses a cached fetch of the robots.txt file, so can be called multiple times without incurring additional
     * overhead on the subject server.
     *
     * @return A list of sitemap URLs found in the robots.txt file.
     * @throws IOException
     * @throws NotValidRobotsTxtException
     */
    static List<String> getSitemapsFromRobotsTxt() throws IOException, NotValidRobotsTxtException {
        return robotsScraper
                .getUserAgents()
                .stream()
                .flatMap(r -> r.getSitemaps().stream())
                .collect(Collectors.toList());
    }

}
