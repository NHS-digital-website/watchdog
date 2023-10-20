package uk.nhs.england.dnslookups;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class DNSLookUpTextRecordExtractor {
    private static final Logger logger = LogManager.getLogger(DNSLookUpTextRecordExtractor.class);
    public static String getTXTFromDNS(String domain) {
        try {
            Lookup lookup = new Lookup(domain, Type.TXT);
            Record[] records = lookup.run();
            if (records != null) {
                for (Record record : records) {
                    return record.rdataToString();
                }
            }
        } catch (Exception e) {
            logger.error("Error getting TXT record for domain: " + domain, e);
        }
        return null;
    }
}


