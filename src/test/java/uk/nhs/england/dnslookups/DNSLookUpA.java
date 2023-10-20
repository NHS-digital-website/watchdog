package uk.nhs.england.dnslookups;

import org.xbill.DNS.*;

public class DNSLookUpA extends DNSLookUp {

    public static boolean contains(Record[] records, String expected) {
        return DNSLookUp.contains(records, expected, record -> ((ARecord) record).getAddress().getHostAddress());
    }

}
