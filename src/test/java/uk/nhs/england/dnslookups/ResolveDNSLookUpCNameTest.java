package uk.nhs.england.dnslookups;

import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResolveDNSLookUpCNameTest {
    private static final Logger logger = LogManager.getLogger(ResolveDNSLookUpCNameTest.class);
    @Test
    public void testValidCNAME() {
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
            if (Objects.equals(record, "CNAME")) {
                String actualCNAME = DNSLookUpCNameExtractor.getCNAMEFromDNS(domain);

                // Assert
                boolean atLeastOneCNAMEMatches = false;
                for (String cname : expected.split(",")) {
                    System.out.println("ACTUAL CNAME:" + actualCNAME + "EXPECTED CNAME:" + cname);
                    if (Objects.equals(cname, actualCNAME)) {
                        assertEquals(cname, actualCNAME);
                        atLeastOneCNAMEMatches = true;
                    }
                }
               if (!atLeastOneCNAMEMatches) {
                   logger.error("FAILED ON: " + domain + " Expected CNAME: " + expected + " Actual CNAME: " + actualCNAME);
                    assertEquals(expected, actualCNAME);
                }
            }
        }
    }
}