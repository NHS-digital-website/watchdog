package uk.nhs.england.redirection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import uk.nhs.england.utils.Utils;
import uk.nhs.england.tags.Production;
import uk.nhs.england.utils.helpers.BasicAuthenticatorExtension;
import uk.nhs.england.utils.helpers.SystemProperties;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.nhs.england.utils.Utils.*;

@ExtendWith(BasicAuthenticatorExtension.class)
public class RedirectionTest {

    private static final Logger logger = LogManager.getLogger(RedirectionTest.class);

    /**
     * This test checks the old Developer Network links continue to redirect to digital.nhs.uk correctly.
     */
    @ParameterizedTest @Production
    @CsvFileSource(resources = "/redirections/old-developer-network-links.csv", numLinesToSkip = 1)
    void developerNetworkRedirectionsTest(String originalLocation, String newLocation) throws IOException {
        testRedirection("Developer Network", originalLocation, newLocation);
    }

    /**
     * This test checks a sample of the CMS managed redirect, to confirm the CMS's redirect system is likely configured
     * as expected.
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/redirections/cms-managed-links.csv", numLinesToSkip = 1)
    void cmsRedirectionsTest(String originalLocation, String newLocation) throws IOException {
        StringBuilder target = new StringBuilder(originalLocation);
        if(!hasAProtocolAndAuthority(originalLocation)) {
            target.insert(0, SystemProperties.getProtocol() + "://" + SystemProperties.getDomain());
        }
        testRedirection("CMS Managed Redirects", target.toString(), newLocation);
    }

     /**
     * Check that the HSCIC redirection to digital works 
     * as expected.
     */
    @ParameterizedTest @Production
    @CsvFileSource(resources = "/redirections/redirection-links.csv", numLinesToSkip = 1)
    void redirectionsTest(String originalLocation, String newLocation) throws IOException {

        testRedirection("HSCIC Redirects", originalLocation, newLocation);
    }


    public void testRedirection(final String groupName, final String originalLocation, final String newLocation) throws IOException {
        // Set up jump logging
        List<Jump> jumps = new ArrayList<>();
        Jump jump = new Jump(originalLocation);

        // We need to fail the test if this throws an exception.
        HttpURLConnection connection = connectTo(originalLocation);
        boolean hadException = false;

        try {
            while (connection.getResponseCode() == 301) {
                // Record the jump
                jumps.add(jump.setStop(qualifiedLocation(connection)));
                jump = new Jump(qualifiedLocation(connection));

                connection = connectTo(qualifiedLocation(connection));
            }
        } catch (IOException e) {
            hadException = true;
            logResults(groupName + ":", jumps, e.getClass().getSimpleName());
        } finally {

            // Record the final jump and log the results
            jumps.add(jump);
            jump.setStop(null);
            if (!hadException) {
                logResults(groupName + ":", jumps, String.valueOf(connection.getResponseCode()));
            }
            connection.disconnect();
        }

        assertTrue(jumps.stream().anyMatch(j -> j.getStop() != null && j.getStop().endsWith(newLocation)));
    }

    private static HttpURLConnection connectTo(String originalLocation) throws IOException {
        HttpURLConnection connection = Utils.openDecoratedConnection(new URL(originalLocation));
        connection.setInstanceFollowRedirects(false);
        return connection;
    }

    /**
     * A helper class for logging.
     */
    private static class Jump {

        private final String start;
        private String stop;

        public Jump(String start) {
            this.start = start;
        }

        public String getStart() {
            return start;
        }

        public String getStop() {
            return stop;
        }

        public Jump setStop(String stop) {
            this.stop = stop;
            return this;
        }

    }

    private static void logResults(String message, List<Jump> jumps, String outcome) {
        StringBuilder fullMessage = new StringBuilder(message + " \n");
        int count = 0;
        for (Jump jump : jumps) {
            if(jump.getStop() != null) {
                fullMessage.append(jump.getStart()).append(" --> ").append(jump.getStop()).append("\n");
                count++;
            }
        }
        if(jumps.size() == 1) {
            fullMessage.append(jumps.get(0).getStart()).append("\n");
        }
        fullMessage.append("Total Redirects: ").append(count).append("\n");
        fullMessage.append("Final Outcome: ").append(outcome).append("\n");

        logger.info(fullMessage.toString());
    }
}
