package uk.nhs.england.utils.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.nhs.england.utils.Utils;

import java.util.Base64;

import static uk.nhs.england.utils.helpers.SystemProperties.getAuthType;

public class BasicAuth {

    private static final Logger logger = LogManager.getLogger(Utils.class);

    public static boolean usingBasicAuth() {
        String authType = getAuthType();
        return (authType != null && authType.equalsIgnoreCase("basic"));
    }

    public static String basicAuthValue() {
        return "Basic " + Base64.getEncoder().encodeToString((
                System.getProperty("username") + ":" + System.getProperty("password")).getBytes());
    }
}
