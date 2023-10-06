package uk.nhs.england.sitemaps;

import org.junit.jupiter.api.Test;
import uk.nhs.england.Utils;
import uk.nhs.england.sitemaps.exceptions.NotValidRobotsTxtException;

import static org.junit.jupiter.api.Assertions.*;
import static uk.nhs.england.sitemaps.RobotsScraperUtils.getSitemapsFromRobotsTxt;

import java.io.IOException;
import java.net.URL;

public class RobotsScraperTest {

    @Test
    public void robotsHasASitemap() throws IOException, NotValidRobotsTxtException {
        assertTrue(getSitemapsFromRobotsTxt().size() > 0);
    }

    @Test
    public void robotsSitemapIsValidUrl() throws IOException, NotValidRobotsTxtException {
        for(String sitemapUrl: getSitemapsFromRobotsTxt()) {
            assertDoesNotThrow(() -> {
                new URL(sitemapUrl);
            });
        }
    }

    @Test
    public void robotsSitemapDomainSameDomain() throws IOException, NotValidRobotsTxtException {
        for(String sitemapUrl: getSitemapsFromRobotsTxt()) {
            URL url = new URL(sitemapUrl);
            assertEquals(Utils.getDomain(), url.getAuthority());
        }
    }


}
