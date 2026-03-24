package uk.nhs.england.utils.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A proxy class for System.getProperty() that providing the caller instructions about properties that are not
 * set correctly.
 */
public class SystemProperties {

    private static final Logger logger = LogManager.getLogger(SystemProperties.class);

    private enum SystemPropertyKey {
        DOMAIN("domain"),
        PROTOCOL("protocol"),
        USERNAME("username"),
        PASSWORD("password"),
        AUTH_TYPE("authType"),
        USER_AGENT("userAgent"),
        CHECK_IP_FILTER("CheckIpFilter");

        private final String key;

        SystemPropertyKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public static String getDomain() {
        return getRequiredSystemPropertyOrThrowFor(SystemPropertyKey.DOMAIN);
    }

    public static String getProtocol() {
        return getOptionalSystemPropertyFor(SystemPropertyKey.PROTOCOL, "https");
    }

    public static String getUsername() {
        return getRequiredSystemPropertyOrThrowFor(SystemPropertyKey.USERNAME);
    }

    public static String getPassword() {
        return getRequiredSystemPropertyOrThrowFor(SystemPropertyKey.PASSWORD);
    }

    public static String getAuthType() {
        return getOptionalSystemPropertyFor(SystemPropertyKey.AUTH_TYPE);
    }

    public static String getUserAgent() {
        return getOptionalSystemPropertyFor(SystemPropertyKey.USER_AGENT, "Mozilla/5.0");
    }

    public static boolean isIpFilterCheckEnabled() {
        String value = getOptionalSystemPropertyFor(SystemPropertyKey.CHECK_IP_FILTER);
        if (value == null) {
            return false;
        }
        return value.isEmpty() || Boolean.parseBoolean(value);
    }


    private static String getRequiredSystemPropertyOrThrowFor(SystemPropertyKey key) {
        String value = System.getProperty(key.getKey());
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException(key + " property is not set correctly.");
        }
        return value;
    }

    private static String getOptionalSystemPropertyFor(SystemPropertyKey key) {
        return System.getProperty(key.getKey());
    }

    private static String getOptionalSystemPropertyFor(SystemPropertyKey key, String defaultValue) {
        return System.getProperty(key.getKey(), defaultValue);
    }

}
