package uk.nhs.england.dnslookups;

import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResolveDNSLookUpSOATest {
    private static final Logger logger = LogManager.getLogger(ResolveDNSLookUpSOATest.class);
    private final List<String> errors = new ArrayList<>();

    @Test
    public void testValidCSOA() {
        // Arrange
        ReadDNSConfigFile getRecordCount = new ReadDNSConfigFile();
        int recordCount = getRecordCount.getTotalRecords();
        ReadDNSConfigFile getRecord = new ReadDNSConfigFile();

        // Act
        for (int i = 0; i < recordCount; i++) {
            CSVRecord csvRecord = getRecord.getConfigRecord(i);
            String record = csvRecord.get(0);
            String domain = csvRecord.get(1);
            String expected = csvRecord.get(2);


            if (Objects.equals(record, "SOA ADMIN")) {
                String actualSOAAdmin = DNSLookUpSOAExtractor.getSOAAdminFromDNS(domain);
                // Assert SOA Admin
                try {
                    for (String soa : expected.split(",")) {
                        System.out.println("ACTUAL SOA Admin:" + actualSOAAdmin + "EXPECTED SOA Admin:" + soa);
                        assertEquals(soa, actualSOAAdmin);
                    }
                } catch (AssertionError e) {
                    errors.add(e.getMessage());
                }
            }

            if (Objects.equals(record, "SOA HOST")) {
                String actualSOAHost = DNSLookUpSOAExtractor.getSOAHostFromDNS(domain);
                // Assert SOA Host
                try {
                    for (String soa : expected.split(",")) {
                        System.out.println("ACTUAL SOA Host:" + actualSOAHost + "EXPECTED SOA Host:" + soa);
                        assertEquals(soa, actualSOAHost);
                    }
                } catch (AssertionError e) {
                    errors.add(e.getMessage());
                }
            }


            if (Objects.equals(record, "SOA TIME")) {
                String actualSOATime = DNSLookUpSOAExtractor.getSOATimeFromDNS(domain);
                // Assert SOA Time
                try {
                    for (String soa : expected.split(",")) {
                        System.out.println("ACTUAL SOA Time:" + actualSOATime + "EXPECTED SOA Time:" + soa);
                        assertEquals(soa, actualSOATime);
                    }
                } catch (AssertionError e) {
                    errors.add(e.getMessage());
                }
            }
        }

        if (!errors.isEmpty()) {
            reportErrors(errors);
        }
    }

    private void reportErrors(List<String> errors) {
        for (String error : errors) {
            logger.error("Report Errors for SOA Lookup: " + error);
        }
        Assertions.fail("Test failed with " + errors.size() + " error(s).");
    }
}
