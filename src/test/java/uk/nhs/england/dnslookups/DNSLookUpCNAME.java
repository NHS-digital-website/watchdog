package uk.nhs.england.dnslookups;

import org.xbill.DNS.*;

public class DNSLookUpCNAME extends DNSLookUp {

    public static boolean contains(Record[] records, String expected) {
        return DNSLookUp.contains(records, expected, record -> ((CNAMERecord) record).getTarget().toString());
    }

}


