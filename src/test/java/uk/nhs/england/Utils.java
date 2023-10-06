package uk.nhs.england;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {

    private static final Logger logger = LogManager.getLogger(Utils.class);

    public static String getDomain() {
        String domain = System.getProperty("domain");
        if (domain == null) {
            throw new RuntimeException("domain property is not set");
        } else {
            logger.info("Domain property is set to " + domain);
        }
        return domain;
    }
}
