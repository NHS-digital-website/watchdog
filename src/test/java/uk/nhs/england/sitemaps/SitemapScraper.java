package uk.nhs.england.sitemaps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import uk.nhs.england.sitemaps.exceptions.NotValidSitemapTypeException;
import uk.nhs.england.utils.helpers.SystemProperties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SitemapScraper {
    private static final Logger logger = LogManager.getLogger(SitemapScraper.class);
    private final List<URL> urls = new ArrayList<>();

    private final List<String> malformedURLs = new ArrayList<>();

    public SitemapScraper(final String sitemap) throws IOException, NotValidSitemapTypeException {
        logger.info("Fetching sitemap: " + sitemap);
        Document document = Jsoup.connect(sitemap).userAgent(SystemProperties.getUserAgent()).get();

        if (typeOf(document, "UrlSet")) {
            urls.addAll(getLocations(document));
        } else if (typeOf(document, "SitemapIndex")) {
            for (URL mapLocation : getLocations(document)) {
                SitemapScraper sitemapScraper = new SitemapScraper(mapLocation.toString());
                urls.addAll(sitemapScraper.getUrls());
                malformedURLs.addAll(sitemapScraper.getMalformedURLs());
            }
        } else {
            throw new NotValidSitemapTypeException("Not Valid Sitemap Type Exception: It doesn't match any known sitemap type.");
        }
        logger.info("Finished fetching sitemap: " + sitemap);
    }

    /**
     * Safely checks if the document is of the given type.
     */
    private boolean typeOf(Document document, String type) {
        Element root = document.select(":root").first();
        if(root != null) {
            return root.tagName().equalsIgnoreCase(type);
        }
        return false;
    }

    /**
     * Safely creates a URL from a string. If the string representation of the URL is malformed, it will be added to
     * the list of malformed URLs.
     *
     * @param url The string representation of the URL.
     * @return The URL object or null if the URL is malformed.
     */
    private URL createURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            malformedURLs.add(url);
            return null;
        }
    }

    private List<URL> getLocations(Document document) {
        return document.select("loc").stream().map(elm -> createURL(elm.text())).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<URL> getUrls() {
        return Collections.unmodifiableList(urls);
    }

    public List<String> getMalformedURLs() {
        return Collections.unmodifiableList(malformedURLs);
    }
}
