package uk.nhs.england.dnslookups;

import org.xbill.DNS.Record;
import org.xbill.DNS.SOARecord;

public class DNSLookUpSOA extends DNSLookUp {

    public static boolean contains(Record[] records, String expected) {
        return DNSLookUp.contains(records, expected, record -> soaRecordToString((SOARecord) record));
    }

    private static String soaRecordToString(SOARecord soa) {
        return "@ IN SOA " +
                soa.getHost() + " " +
                soa.getAdmin() + " (" +
                soa.getSerial() + " " +
                soa.getRefresh() + " " +
                soa.getRetry() + " " +
                soa.getExpire() + " " +
                soa.getMinimum() + ")";
    }

}
