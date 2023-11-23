package uk.nhs.england.sitemaps;

import org.junit.jupiter.api.Test;
import uk.nhs.england.utils.helpers.SystemProperties;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static uk.nhs.england.sitemaps.RobotsScraperUtils.getSitemapsFromRobotsTxt;

public class RobotsScraperTest {

    @Test
    public void robotsHasASitemap() {
        assertFalse(getSitemapsFromRobotsTxt().isEmpty());
    }

    @Test
    public void robotsSitemapIsValidUrl() {
        for (String sitemapUrl : getSitemapsFromRobotsTxt()) {
            assertDoesNotThrow(() -> {
                new URL(sitemapUrl);
            });
        }
    }

    @Test
    public void robotsSitemapDomainSameDomain() throws IOException {
        for (String sitemapUrl : getSitemapsFromRobotsTxt()) {
            URL url = new URL(sitemapUrl);
            assertEquals(SystemProperties.getDomain(), url.getAuthority());
        }
    }

}
