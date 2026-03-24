package uk.nhs.england.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import uk.nhs.england.utils.Utils;
import uk.nhs.england.utils.helpers.SystemProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IpFilterBlocksTest {

    private static final Logger logger = LogManager.getLogger(IpFilterBlocksTest.class);
    private static final String RESOURCE_PATH = "/security/ip-filter-blocked-addresses.txt";

    @BeforeAll
    static void requireIpFilterFlag() {
        Assumptions.assumeTrue(SystemProperties.isIpFilterCheckEnabled(),
                "Set -DCheckIpFilter (or -DCheckIpFilter=true) to run IP filter checks");
    }

    @ParameterizedTest(name = "{0} should be blocked")
    @MethodSource("blockedAddresses")
    void ipFilterBlocksConfiguredAddresses(String address) throws IOException {
        HttpURLConnection connection = Utils.openDecoratedConnection(new URL(address));
        connection.setInstanceFollowRedirects(false);
        try {
            int responseCode = connection.getResponseCode();
            logger.info("IP filter check for {} returned {}", address, responseCode);
            assertEquals(HttpURLConnection.HTTP_FORBIDDEN, responseCode,
                    () -> "Expected IP filter to block " + address + " but received " + responseCode);
        } finally {
            connection.disconnect();
        }
    }

    private static Stream<String> blockedAddresses() {
        InputStream inputStream = IpFilterBlocksTest.class.getResourceAsStream(RESOURCE_PATH);
        if (inputStream == null) {
            throw new IllegalStateException("Unable to load " + RESOURCE_PATH);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            List<String> addresses = reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .filter(line -> !line.startsWith("#"))
                    .collect(Collectors.toList());

            if (addresses.isEmpty()) {
                throw new IllegalStateException("No addresses configured for IP filter checks in " + RESOURCE_PATH);
            }

            return addresses.stream();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + RESOURCE_PATH, e);
        }
    }
}
