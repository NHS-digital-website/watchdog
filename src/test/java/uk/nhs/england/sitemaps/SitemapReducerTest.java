package uk.nhs.england.sitemaps;

import org.junit.jupiter.api.Test;
import uk.nhs.england.tags.Watchdog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A set of test that check the SitemapReducer 'reduce' method returns a list of URLs that includes just one URL for each
 * branch of a URL hierarchy.
 */
public class SitemapReducerTest {

    @Test
    @Watchdog
    public void testReduceUrls() throws MalformedURLException {
        // Define the list of URLs to reduce
        List<URL> sitemapUrls = new ArrayList<>();
        sitemapUrls.add(new URL("https://example.com/"));
        sitemapUrls.add(new URL("https://example.com/blog"));
        sitemapUrls.add(new URL("https://example.com/blog/item-1"));
        sitemapUrls.add(new URL("https://example.com/blog/item-2"));
        sitemapUrls.add(new URL("https://example.com/blog/item-3"));
        sitemapUrls.add(new URL("https://example.com/news"));
        sitemapUrls.add(new URL("https://example.com/news/gaming"));
        sitemapUrls.add(new URL("https://example.com/news/gaming/item-1"));
        sitemapUrls.add(new URL("https://example.com/news/gaming/item-2"));
        sitemapUrls.add(new URL("https://example.com/news/gaming/item-3"));
        sitemapUrls.add(new URL("https://example.com/news/company"));
        sitemapUrls.add(new URL("https://example.com/news/company/item-1"));
        sitemapUrls.add(new URL("https://example.com/news/company/item-2"));
        sitemapUrls.add(new URL("https://example.com/news/company/item-3"));

        // Reduce the list of URLs
        List<URL> reducedUrls = SitemapReducer.reduce(sitemapUrls);

        // Define the expected result
        List<URL> expectedReducedUrls = new ArrayList<>();
        expectedReducedUrls.add(new URL("https://example.com/"));
        expectedReducedUrls.add(new URL("https://example.com/blog"));
        expectedReducedUrls.add(new URL("https://example.com/blog/item-1"));
        expectedReducedUrls.add(new URL("https://example.com/news"));
        expectedReducedUrls.add(new URL("https://example.com/news/gaming"));
        expectedReducedUrls.add(new URL("https://example.com/news/gaming/item-1"));
        expectedReducedUrls.add(new URL("https://example.com/news/company"));
        expectedReducedUrls.add(new URL("https://example.com/news/company/item-1"));

        // Verify that the reducedUrls list matches the expected result
        assertEquals(expectedReducedUrls.size(), reducedUrls.size());
        for(URL url: expectedReducedUrls) {
            assertTrue(reducedUrls.contains(url));
        }
    }


    @Test
    @Watchdog
    public void testReduceUrls_twoFoldersApart() throws MalformedURLException {
        // Define the list of URLs to reduce
        List<URL> sitemapUrls = new ArrayList<>();
        sitemapUrls.add(new URL("https://example.com/blog/item-1"));
        sitemapUrls.add(new URL("https://example.com/blog/item-2"));
        sitemapUrls.add(new URL("https://example.com/news/gaming/item-1/sub-1/test-1"));
        sitemapUrls.add(new URL("https://example.com/news/gaming/item-1/sub-2/test-1"));

        // Reduce the list of URLs
        List<URL> reducedUrls = SitemapReducer.reduce(sitemapUrls);

        // Define the expected result
        List<URL> expectedReducedUrls = new ArrayList<>();
        expectedReducedUrls.add(new URL("https://example.com/blog/item-1"));
        expectedReducedUrls.add(new URL("https://example.com/news/gaming/item-1/sub-1/test-1"));
        expectedReducedUrls.add(new URL("https://example.com/news/gaming/item-1/sub-2/test-1"));

        // Verify that the reducedUrls list matches the expected result
        assertEquals(expectedReducedUrls.size(), reducedUrls.size());
        for(URL url: expectedReducedUrls) {
            assertTrue(reducedUrls.contains(url));
        }
    }

    /**
     * Or though, technically incorrect to mix domain, this test shows that the reducer will continue to reduce as
     * expected even with mixed domains.
     */
    @Test
    @Watchdog
    public void testReduceUrls_mixedDomains() throws MalformedURLException {
        // Define the list of URLs to reduce
        List<URL> sitemapUrls = new ArrayList<>();
        sitemapUrls.add(new URL("https://example.com/"));
        sitemapUrls.add(new URL("https://example.com/blog/item-1"));
        sitemapUrls.add(new URL("https://example.com/blog/item-2"));
        sitemapUrls.add(new URL("https://demo.com/"));
        sitemapUrls.add(new URL("https://demo.com/blog/item-1"));
        sitemapUrls.add(new URL("https://demo.com/blog/item-2"));
        sitemapUrls.add(new URL("https://example.com/news/gaming/item-1"));
        sitemapUrls.add(new URL("https://example.com/news/gaming/item-2"));
        sitemapUrls.add(new URL("https://demo.com/news/gaming/item-1"));
        sitemapUrls.add(new URL("https://demo.com/news/gaming/item-2"));

        // Reduce the list of URLs
        List<URL> reducedUrls = SitemapReducer.reduce(sitemapUrls);

        // Define the expected result
        List<URL> expectedReducedUrls = new ArrayList<>();

        expectedReducedUrls.add(new URL("https://example.com/"));
        expectedReducedUrls.add(new URL("https://example.com/blog/item-1"));
        expectedReducedUrls.add(new URL("https://demo.com/"));
        expectedReducedUrls.add(new URL("https://demo.com/blog/item-1"));
        expectedReducedUrls.add(new URL("https://example.com/news/gaming/item-1"));
        expectedReducedUrls.add(new URL("https://demo.com/news/gaming/item-1"));

        // Verify that the reducedUrls list matches the expected result
        assertEquals(expectedReducedUrls.size(), reducedUrls.size());
        for(URL url: expectedReducedUrls) {
            assertTrue(reducedUrls.contains(url));
        }
    }

    @Test
    @Watchdog
    public void testReduceUrls_withOrWithoutSlashes() throws MalformedURLException {
        // Define the list of URLs to reduce
        List<URL> sitemapUrls = new ArrayList<>();
        sitemapUrls.add(new URL("https://example.com/blog/item-1"));
        sitemapUrls.add(new URL("https://example.com/blog/item-1/"));
        sitemapUrls.add(new URL("https://example.com/news/item-1"));
        sitemapUrls.add(new URL("https://example.com/news/item-1/"));

        // Reduce the list of URLs
        List<URL> reducedUrls = SitemapReducer.reduce(sitemapUrls);

        // Define the expected result
        List<URL> expectedReducedUrls = new ArrayList<>();

        expectedReducedUrls.add(new URL("https://example.com/blog/item-1"));
        expectedReducedUrls.add(new URL("https://example.com/news/item-1"));

        // Verify that the reducedUrls list matches the expected result
        assertEquals(expectedReducedUrls.size(), reducedUrls.size());
        for(URL url: expectedReducedUrls) {
            assertTrue(reducedUrls.contains(url));
        }
    }

}
