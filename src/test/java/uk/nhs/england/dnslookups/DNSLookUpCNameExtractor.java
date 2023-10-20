package uk.nhs.england.dnslookups;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class DNSLookUpCNameExtractor {
    private static final Logger logger = LogManager.getLogger(DNSLookUpCNameExtractor.class);
    public static String getCNAMEFromDNS(String domain) {
        try {
            Lookup lookup = new Lookup(domain, Type.CNAME);
            Record[] records = lookup.run();
            for (Record record : records) {
                return record.rdataToString();
            }
        } catch (Exception e) {
            logger.error("Error getting CNAME for domain: " + domain, e);

        }
        return null;
    }
}


