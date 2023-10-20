package uk.nhs.england.dnslookups;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class DNSLookUpNameServerExtractor {
    private static final Logger logger = LogManager.getLogger(DNSLookUpNameServerExtractor.class);
    public static String getNSFromDNS(String domain) {
        try {
            Lookup lookup = new Lookup(domain, Type.NS);
            Record[] records = lookup.run();
            if (records != null) {
                for (Record record : records) {
                    return record.rdataToString();
                }
            }
        } catch (Exception e) {
            logger.error("Error getting NS from DNS", e);
        }
        return null;
    }
}


