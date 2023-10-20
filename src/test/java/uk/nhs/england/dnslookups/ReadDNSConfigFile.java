package uk.nhs.england.dnslookups;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadDNSConfigFile {
    private static final Logger logger = LogManager.getLogger(ReadDNSConfigFile.class);
    String csvFile = "src/test/resources/dnsconfigurationforcefail.csv";
    public CSVRecord getConfigRecord(int index) {
        try (FileReader fileReader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT)) {
            int currentIndex = 0;
            for (CSVRecord csvRecord : csvParser) {
                if (currentIndex == index) {
                    return csvRecord;
                }
                currentIndex++;
            }
        } catch (IOException e) {
            logger.error("Error reading CSV file: " + e.getMessage());
        }
        return null;
    }

    public int getTotalRecords() {
        int recordCount = 0;
        try (FileReader fileReader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT)) {
            List<CSVRecord> records = new ArrayList<>();
            for (CSVRecord csvRecord : csvParser) {
                recordCount++;
            }
            return recordCount;
        } catch (IOException e) {
            logger.error("Error reading CSV file for total records: " + e.getMessage());
        }
        return 0;
    }
}