package uk.nhs.england.dnslookups;

import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

public class ResolveIpAddressesTest {
    private static final Logger logger = LogManager.getLogger(ResolveIpAddressesTest.class);
    private final List<String> errors = new ArrayList<>();

    @Test
    public void testValidURL() {
        // Arrange
        ReadDNSConfigFile getRecordCount = new ReadDNSConfigFile();
        int recordCount = getRecordCount.getTotalRecords();
        ReadDNSConfigFile getRecord = new ReadDNSConfigFile();

        Set<String> checkActualIP = new HashSet<>();
        Set<String> checkExpectedIP = new HashSet<>();

        Set<String> checkActualIPCMS = new HashSet<>();
        Set<String> checkExpectedIPCMS = new HashSet<>();

        // Act
        for (int i = 0; i < recordCount; i++) {
            CSVRecord csvRecord = getRecord.getConfigRecord(i);
            String record = csvRecord.get(0);
            String domain = csvRecord.get(1);
            String expected = csvRecord.get(2);

            String[] expectedIPs = new String[]{expected};

            // test non CMS domains
            if (Objects.equals(record, "A")) {
                String actualIPAddress = IPAddressExtractor.getIPAddressFromURL("https://" + domain);
                String[] ipAddresses = new String[]{actualIPAddress};

                Collections.addAll(checkActualIP, ipAddresses);
                Collections.addAll(checkExpectedIP, expectedIPs);
            }

            // test CMS domains
            if (Objects.equals(record, "A CMS")) {
                String actualIPAddressCMS = IPAddressExtractor.getIPAddressFromURL("https://" + domain);
                String[] ipAddressesCMS = new String[]{actualIPAddressCMS};

                Collections.addAll(checkActualIPCMS, ipAddressesCMS);
                Collections.addAll(checkExpectedIPCMS, expectedIPs);
            }
        }
        // Assert Non CMS domains
        try {
            for (String thisActualIP : checkActualIP) {
                System.out.println("Actual NON CMS IP: " + thisActualIP + " Expected: " + checkExpectedIP);
                assertThat("Actual NON CMS IP: " + thisActualIP, checkExpectedIP, hasItem(thisActualIP));
            }
        } catch (AssertionError e) {
            errors.add(e.getMessage());
        }


        // Assert CMS domains
        try {
            for (String thisActualIPCMS : checkActualIPCMS) {
                System.out.println("Actual CMS IP: " + thisActualIPCMS + " Expected: " + checkExpectedIPCMS);
                assertThat("Actual CMS IP: " + thisActualIPCMS, checkExpectedIPCMS, hasItem(thisActualIPCMS));
            }
        } catch (AssertionError e) {
            errors.add(e.getMessage());
        }
        if (!errors.isEmpty()) {
            reportErrors(errors);
        }
    }

    private void reportErrors(List<String> errors) {
        for (String error : errors) {
            logger.error("Report Errors for IP Addresses: " + error);
        }
        Assertions.fail("Test failed with " + errors.size() + " error(s).");
    }
}