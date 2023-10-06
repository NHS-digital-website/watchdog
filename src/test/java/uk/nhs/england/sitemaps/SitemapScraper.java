package uk.nhs.england.sitemaps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import uk.nhs.england.sitemaps.exceptions.NotValidSitemapTypeException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This Scraper fetches a full list of URLs from a given sitemap.
 *
 * If it encounters a sitemap index, it will fetch all the sitemaps in the index and combine the results.
 */
public class SitemapScraper {

    private static final Logger logger = LogManager.getLogger(SitemapScraper.class);

    private List<URL> urls = new ArrayList<>();

    public SitemapScraper(final String sitemap) throws IOException, NotValidSitemapTypeException {

        logger.info("Fetching sitemap: " + sitemap);

        // Get the Sitemap
        Document document = Jsoup.connect(sitemap).get();
        if (typeOf(document, "UrlSet")) {
            // get the links form the sitemaps
            urls.addAll(getLocations(document));
        } else if(typeOf(document, "SitemapIndex")) {
            // get the combined links form the sitemaps in the sitemap index.
            for(URL mapLocation: getLocations(document)) {
                urls.addAll(new SitemapScraper(mapLocation.toString()).getUrls());
            }
        } else {
            throw new NotValidSitemapTypeException("It doesn't match any known sitemap type.");
        }

        logger.info("Finished fetching sitemap: " + sitemap);
    }

    private boolean typeOf(Document document, String type) {
        return document.select(":root").first().tagName().equalsIgnoreCase(type);
    }

    private List<URL> getLocations(Document document) {
        return document.select("loc") // Assuming URLs are enclosed in <loc> tags
                .stream()
                .map(e -> {
                    try {
                        return new URL(e.text());
                    } catch (MalformedURLException ex) {
                        throw new RuntimeException(ex);
                    }
                }) // Extract text content of each element
                .collect(Collectors.toList());
    }

    public List<URL> getUrls() {
        return Collections.unmodifiableList(urls);
    }
}
