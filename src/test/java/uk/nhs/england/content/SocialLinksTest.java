package uk.nhs.england.content;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import uk.nhs.england.utils.helpers.SystemProperties;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SocialLinksTest {

    private static final Logger logger = LogManager.getLogger(SocialLinksTest.class);
    private static SocialLinks fetcher;
    public static final String ADDRESS = SystemProperties.getProtocol() + "://" + SystemProperties.getDomain();
    private static final Function<Document, Elements> FIND_FUNCTION = doc -> Objects.requireNonNull(
            Objects.requireNonNull(
                    doc.selectFirst("#footer-section-wrapper-social-media")
            ).parent()
    ).select("a.nhsd-a-link");

    @BeforeAll
    public static void setUp() {
        fetcher = new SocialLinks(
                ADDRESS,
                FIND_FUNCTION
        );
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/content/social-links.csv", useHeadersInDisplayName = true)
    public void checkTheSocialLinksArePresent(String service, String link) {
        List<String> links = fetcher.getLinks();

        try {
            if (links.isEmpty()) {
                fail("Social media links not found");
            }
            assertTrue(links.contains(link), "Social media " + service + " link not found: " + link);
        } catch (AssertionError e) {
            // Log the assertion error so that it can be seen in the test report.
            logger.error(e.getMessage());
            throw e;
        }

        logger.info("Social media " + service + " link found: " + link);
    }
}
