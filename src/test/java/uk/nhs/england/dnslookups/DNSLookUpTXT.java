package uk.nhs.england.dnslookups;

import org.xbill.DNS.Record;
import org.xbill.DNS.TXTRecord;

import java.util.Arrays;

public class DNSLookUpTXT extends DNSLookUp {

    public static boolean contains(Record[] records, String expected) {
        return Arrays.stream(records)
                .filter(record -> record instanceof TXTRecord)
                .flatMap(record -> ((TXTRecord) record).getStrings().stream())
                .anyMatch(txt -> txt.equalsIgnoreCase(expected));
    }

}
