package uk.nhs.england.dnslookups;

import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResolveDNSLookUpNameServerTest {
    private static final Logger logger = LogManager.getLogger(ResolveDNSLookUpNameServerTest.class);
    @Test
    public void testValidNS() {
        // Arrange
        ReadDNSConfigFile getRecordCount = new ReadDNSConfigFile();
        int recordCount = getRecordCount.getTotalRecords();
        ReadDNSConfigFile getRecord = new ReadDNSConfigFile();

        Set<String> checkActualNS = new HashSet<>();
        Set<String> checkExpectedNS = new HashSet<>();

        // Act
        for (int i = 0; i < recordCount; i++) {
            CSVRecord csvRecord = getRecord.getConfigRecord(i);
            String record = csvRecord.get(0);
            String domain = csvRecord.get(1);
            String expected = csvRecord.get(2);

            String actualNS = DNSLookUpNameServerExtractor.getNSFromDNS(domain);
            if(actualNS != null) {
                String[] nameServers = new String[]{actualNS};
                Collections.addAll(checkActualNS, nameServers);
            }

            String[] expectedNS = new String[]{expected};

            if (Objects.equals(record, "NS")) {
                Collections.addAll(checkExpectedNS, expectedNS);
            }
        }

        // Assert
        boolean matchFound = false;
        for (String thisActual : checkActualNS) {
            for (String thisExpected : checkExpectedNS) {
                if (thisActual.equals(thisExpected)) {
                    System.out.println("Match: Actual NS = " + thisActual + ", Expected NS = " + thisExpected);
                    assertEquals(thisActual, thisExpected);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                logger.error("FAILED ON: " + thisActual + " Expected NS: " + checkExpectedNS + " Actual NS: " + checkActualNS);
                assertEquals(checkExpectedNS, checkActualNS);
            }
        }
    }
}
