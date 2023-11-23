package uk.nhs.england.dnslookups;

import org.xbill.DNS.NSRecord;
import org.xbill.DNS.Record;

public class DNSLookUpNS extends DNSLookUp {

    public static boolean contains(Record[] records, String expected) {
        return DNSLookUp.contains(records, expected, record -> ((NSRecord) record).getTarget().toString());
    }
}
