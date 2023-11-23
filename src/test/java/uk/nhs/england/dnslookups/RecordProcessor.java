package uk.nhs.england.dnslookups;

import org.xbill.DNS.Record;

@FunctionalInterface
public interface RecordProcessor<T extends Record> {
    String process(T record);
}
