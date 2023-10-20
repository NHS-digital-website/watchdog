package uk.nhs.england.dnslookups;

import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;

public class ResolveDNSLookUpTextRecordTest {
    private static final Logger logger = LogManager.getLogger(ResolveDNSLookUpTextRecordTest.class);
    private final List<String> errors = new ArrayList<>();
    @Test
    public void testValidTXTRecord() {
        // Arrange
        ReadDNSConfigFile getRecordCount = new ReadDNSConfigFile();
        int recordCount = getRecordCount.getTotalRecords();
        ReadDNSConfigFile getRecord = new ReadDNSConfigFile();

        Set<String> actualTXTRecords = new HashSet<>();
        Set<String> expectedTXTRecordsGoogle = new HashSet<>();
        Set<String> expectedTXTRecordsV = new HashSet<>();

        Set<String> matchedTXTRecords = new HashSet<>();


        // Act
        for (int i = 0; i < recordCount; i++) {
            CSVRecord csvRecord = getRecord.getConfigRecord(i);
            String record = csvRecord.get(0);
            String domain = csvRecord.get(1);
            String expected = csvRecord.get(2);

            String actualTXT = DNSLookUpTextRecordExtractor.getTXTFromDNS(domain);
            if (actualTXT != null) {
                String cleanedTXT = actualTXT.replace("\"", "");
                actualTXTRecords.add(cleanedTXT);
            }

            String[] expectedTextRecords = new String[]{expected};

            switch (record) {
                case "TXT GOOGLE":
                    Collections.addAll(expectedTXTRecordsGoogle, expectedTextRecords);
                    break;
                case "TXT V":
                    Collections.addAll(expectedTXTRecordsV, expectedTextRecords);
                    break;
            }
        }

        matchedTXTRecords.addAll(expectedTXTRecordsGoogle);
        matchedTXTRecords.addAll(expectedTXTRecordsV);

        // Assert
        for (String matched : matchedTXTRecords) {
            if (actualTXTRecords.contains(matched)) {
                System.out.println("matched: " + matched);
                MatcherAssert.assertThat(actualTXTRecords, Matchers.hasItem(matched));
            }
            else {
                errors.add(matched);
            }
        }

        if(!errors.isEmpty()) {
            reportErrors(errors);
        }
    }
    private void reportErrors(List<String> errors) {
        for (String error : errors) {
            logger.error("Report Errors for Text Record Lookup: " + error);
        }
        Assertions.fail("Test failed with " + errors.size() + " error(s).");
    }
}
