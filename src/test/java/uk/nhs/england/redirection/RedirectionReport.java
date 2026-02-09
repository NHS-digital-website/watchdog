package uk.nhs.england.redirection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Writes out a CSV report of every redirect that was exercised while the tests ran.
 */
public final class RedirectionReport {

    private static final Logger logger = LogManager.getLogger(RedirectionReport.class);
    private static final Path REPORT_PATH = Paths.get("logs", "watchdog-redirections-report.csv");
    private static final byte[] HEADER =
            "Group,Original Domain,Original Location,Expected Location,Final Location,Outcome,Outcome Domain\n"
                    .getBytes(StandardCharsets.UTF_8);

    static {
        initialiseReport();
    }

    private RedirectionReport() {
    }

    private static void initialiseReport() {
        try {
            Files.createDirectories(REPORT_PATH.getParent());
            Files.write(REPORT_PATH, HEADER, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            logger.warn("Unable to initialise redirections report at {}", REPORT_PATH, e);
        }
    }

    public static synchronized void append(String group,
                                           String originalLocation,
                                           String expectedLocation,
                                           String finalLocation,
                                           String outcome) {
        try {
            Files.write(REPORT_PATH,
                    formatRow(group, originalLocation, expectedLocation, finalLocation, outcome),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.warn("Unable to update redirections report at {}", REPORT_PATH, e);
        }
    }

    private static byte[] formatRow(String group,
                                    String originalLocation,
                                    String expectedLocation,
                                    String finalLocation,
                                    String outcome) {
        String originalDomain = extractDomain(originalLocation);
        String outcomeDomain = extractDomain(finalLocation);
        String row = new StringBuilder()
                .append(csvCell(group)).append(',')
                .append(csvCell(originalDomain)).append(',')
                .append(csvCell(originalLocation)).append(',')
                .append(csvCell(expectedLocation)).append(',')
                .append(csvCell(finalLocation)).append(',')
                .append(csvCell(outcome)).append(',')
                .append(csvCell(outcomeDomain))
                .append('\n')
                .toString();
        return row.getBytes(StandardCharsets.UTF_8);
    }

    private static String extractDomain(String location) {
        if (location == null || location.isEmpty()) {
            return "";
        }
        try {
            return new java.net.URL(location).getHost();
        } catch (Exception ignored) {
            return "";
        }
    }

    private static String csvCell(String value) {
        if (value == null) {
            return "";
        }
        return '"' + value.replace("\"", "\"\"") + '"';
    }
}
