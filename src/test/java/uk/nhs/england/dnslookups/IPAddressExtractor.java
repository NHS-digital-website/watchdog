package uk.nhs.england.dnslookups;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPAddressExtractor {
    private static final Logger logger = LogManager.getLogger(IPAddressExtractor.class);
    public static String getIPAddressFromURL(String url) {
        try {
            Pattern pattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)");
            Matcher matcher = pattern.matcher(url);

            if (matcher.find()) {
                return matcher.group(0);
            } else {
                try {
                    InetAddress inetAddress = InetAddress.getByName(new java.net.URL(url).getHost());
                    return inetAddress.getHostAddress();
                } catch (java.net.UnknownHostException e) {
                    logger.error("Unable to resolve IP address for URL - Unknown Host: " + url);
                }
            }
        } catch (java.net.MalformedURLException e) {
            logger.error("Unable to resolve IP address for URL - Malformed: " + url);
            return null;
        }
    return null;
    }
}

