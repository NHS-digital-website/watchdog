package uk.nhs.england.sitemaps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.nhs.england.Utils;
import uk.nhs.england.sitemaps.exceptions.NotValidRobotsTxtException;
import uk.nhs.england.sitemaps.exceptions.NotValidSitemapTypeException;
import uk.nhs.england.sitemaps.models.Result;
import uk.nhs.england.tags.Production;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.nhs.england.sitemaps.RobotsScraperUtils.getSitemapsFromRobotsTxt;

public class SitemapScraperTest {

    private static final Logger logger = LogManager.getLogger(SitemapScraperTest.class);

    private static final Set<URL> urls = new HashSet<>();
    private static final Set<String> malformedURLs = new HashSet<>();

    @BeforeAll
    public static void setup() throws IOException, NotValidSitemapTypeException {
        logger.info("Setting up SitemapScraperTest");
        for (String sitemap : getSitemapsFromRobotsTxt()) {
            SitemapScraper scraper = new SitemapScraper(sitemap);
            urls.addAll(scraper.getUrls());
            malformedURLs.addAll(scraper.getMalformedURLs());
        }
    }

    private static void dumpURLs(String message, List<URL> urls) {
        StringBuilder fullListMessage = new StringBuilder(message + "\n");
        for (URL url : urls) {
            fullListMessage.append(url.toString()).append("\n");
        }
        fullListMessage.append("Total: ").append(urls.size()).append("\n");

        logger.info(fullListMessage.toString());
    }

    private static void dumpMalformedURLs(String message, List<String> urls) {
        StringBuilder fullListMessage = new StringBuilder(message + "\n");
        for (String url : urls) {
            fullListMessage.append(url).append("\n");
        }
        fullListMessage.append("Total: ").append(urls.size()).append("\n");

        logger.info(fullListMessage.toString());
    }

    private static void dumpResults(String message, List<Result> results) {
        StringBuilder fullListMessage = new StringBuilder(message + "\n");
        for (Result result : results) {
            fullListMessage.append(result.getUrl().toString()).append(", ").append(result.getResponseCode()).append("\n");
        }
        fullListMessage.append("Total: ").append(results.size()).append("\n");

        logger.info(fullListMessage.toString());
    }

    @Test
    @Production // FIXME: Remove @Production annotation once the production domain is no longer hardcoded into the CMS' sitemaps.
    public void noMalformedUrlInSitemap() {
        if(!malformedURLs.isEmpty()) {
            dumpMalformedURLs("The malformed URLs are", new ArrayList<>(malformedURLs));
        }
        assertTrue(malformedURLs.isEmpty());
    }

    @Test
    @Production // FIXME: Remove @Production annotation once the production domain is no longer hardcoded into the CMS' sitemaps.
    public void sampleCombinedSitemapsForErrors() throws IOException, NotValidRobotsTxtException, NotValidSitemapTypeException {
        logger.info("Beginning the Sitemap Test");

        dumpURLs("Full list of URLs from the sitemaps(s) are", new ArrayList<>(urls));

        List<URL> reducedUrls = SitemapReducer.reduce(new ArrayList<>(urls));

        dumpURLs("The reduced list of URLs from the sitemaps(s) are", reducedUrls);

        List<Result> results = new ArrayList<>();

        for (URL url : SitemapReducer.reduce(new ArrayList<>(urls))) {

            logger.info("Now testing " + url.toString());

            HttpURLConnection connection = Utils.openConnection(url);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            connection.disconnect();

            results.add(new Result(url, responseCode));

            try {
                Thread.sleep(50); // sleep to avoid overloading the server
            } catch (InterruptedException e) {
                logger.warn("Sleep interrupted.", e);
            }
        }

        dumpResults("None okay results", results.stream().filter(r -> r.getResponseCode() != HTTP_OK).collect(Collectors.toList()));
        dumpResults("Okay result results", results.stream().filter(r -> r.getResponseCode() == HTTP_OK).collect(Collectors.toList()));

        // FIXME: Check for 404 errors once the CMS' sitemaps are fixed.
        assertTrue(results.stream().noneMatch(r -> r.getResponseCode() >= 500 && r.getResponseCode() < 600));

    }
}
