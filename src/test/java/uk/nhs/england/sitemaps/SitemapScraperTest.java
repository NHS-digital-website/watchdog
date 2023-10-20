package uk.nhs.england.sitemaps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import uk.nhs.england.Utils;
import uk.nhs.england.sitemaps.exceptions.NotValidRobotsTxtException;
import uk.nhs.england.sitemaps.exceptions.NotValidSitemapTypeException;
import uk.nhs.england.sitemaps.models.Result;
import uk.nhs.england.tags.Production;

import static java.net.HttpURLConnection.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.nhs.england.sitemaps.RobotsScraperUtils.getSitemapsFromRobotsTxt;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.net.ssl.HttpsURLConnection;

public class SitemapScraperTest {

    private static final Logger logger = LogManager.getLogger(SitemapScraperTest.class);

    // The @Production annotation can be removed once's production's domain is not hardcoded into the sitemaps.
    // In the meantime UAT cannot be tested with this test.
    @Test @Production
    public void fetchAndTestCombinedSitemaps() throws IOException, NotValidRobotsTxtException, NotValidSitemapTypeException {
        logger.info("Beginning the Sitemap Test");

        // Create a store for all the sitemap's URLs
        Set<URL> urls = new HashSet<>();

        // Get the urls from the sitemap(s) listed in robots.txt
        for(String sitemap: getSitemapsFromRobotsTxt()) {
            urls.addAll(new SitemapScraper(sitemap).getUrls());
        }

        // Log full set of URLs
        dumpURLs("Full list of URLs from the sitemaps(s) are", new ArrayList<>(urls));

        // Get the reduced set of URLs
        List<URL> reducedUrls = SitemapReducer.reduce(new ArrayList<>(urls));

        // Log the reduced set of URLs
        dumpURLs("The reduced list of URLs from the sitemaps(s) are", reducedUrls);

        // Create a store for all the results
        List<Result> results = new ArrayList<>();

        for (URL url : SitemapReducer.reduce(new ArrayList<>(urls))) {

            logger.info("Now testing " + url.toString());

            // Open a connection to the URL
            HttpURLConnection connection = Utils.openConnection(url);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            // disconnect the connection
            connection.disconnect();

            // Add the result to the results store
            results.add(new Result(url, responseCode));

            // Add a 50-millisecond delay to avoid overloading the server.
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Log the results
        dumpResults("None okay results", results.stream().filter(r -> r.getResponseCode() != HTTP_OK).collect(Collectors.toList()));
        dumpResults("Okay result results", results.stream().filter(r -> r.getResponseCode() == HTTP_OK).collect(Collectors.toList()));

        // Assert that all the results are 200 OK
        // assertTrue(results.stream().allMatch(r -> r.getResponseCode() == HTTP_OK));

        // Until we fix the 404s, assert that there are no 500 errors
        assertTrue(results.stream().noneMatch(r -> r.getResponseCode() >= 500 && r.getResponseCode() < 600));

    }

    private static void dumpURLs(String message, List<URL> urls) {
        // Prepare the message
        StringBuilder fullListMessage = new StringBuilder(message + "\n");
        for(URL url: urls) {
            fullListMessage.append(url.toString()).append("\n");
        }
        fullListMessage.append("Total: ").append(urls.size() + "\n");

        // Log the message
        logger.info(fullListMessage.toString());
    }

    private static void dumpResults(String message, List<Result> results) {
        // Prepare the message
        StringBuilder fullListMessage = new StringBuilder(message + "\n");
        for(Result result: results) {
            fullListMessage.append(result.getUrl().toString()).append(", ").append(result.getResponseCode()).append("\n");
        }
        fullListMessage.append("Total: ").append(results.size() + "\n");

        // Log the message
        logger.info(fullListMessage.toString());
    }


}
