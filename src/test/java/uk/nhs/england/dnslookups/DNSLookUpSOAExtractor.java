package uk.nhs.england.dnslookups;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.Type;

public class DNSLookUpSOAExtractor {
    private static final Logger logger = LogManager.getLogger(DNSLookUpSOAExtractor.class);
    public static String getSOAAdminFromDNS(String domain) {
        try {
            Lookup lookup = new Lookup(domain, Type.SOA);
            Record[] records = lookup.run();
            for (Record record : records) {
                if (record instanceof SOARecord) {
                    SOARecord soaRecord = (SOARecord) record;
                    return soaRecord.getAdmin().toString();
                }
            }
        } catch (Exception e) {
            logger.error("Error getting SOA Admin from DNS", e);
        }
        return null;
    }
    public static String getSOAHostFromDNS(String domain) {
        try {
            Lookup lookup = new Lookup(domain, Type.SOA);
            Record[] records = lookup.run();
            for (Record record : records) {
                if (record instanceof SOARecord) {
                    SOARecord soaRecord = (SOARecord) record;
                    return soaRecord.getHost().toString();
                }
            }
        } catch (Exception e) {
            logger.error("Error getting SOA Host from DNS", e);
        }
        return null;
    }
    public static String getSOATimeFromDNS(String domain) {
        try {
            Lookup lookup = new Lookup(domain, Type.SOA);
            Record[] records = lookup.run();
            for (Record record : records) {
                if (record instanceof SOARecord) {
                    return getSOAString((SOARecord) record);
                }
            }
        } catch (Exception e) {
            logger.error("Error getting SOA Time from DNS", e);
        }
        return null;
    }

    private static String getSOAString(SOARecord record) {
        String soaValues = "";
        soaValues += record.getSerial();
        soaValues += " ";
        soaValues += record.getRefresh();
        soaValues += " ";
        soaValues += record.getRetry();
        soaValues += " ";
        soaValues += record.getExpire();
        soaValues += " ";
        soaValues += record.getMinimum();
        return soaValues;
    }
}
