package uk.nhs.england.dnslookups;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xbill.DNS.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.text.MessageFormat.format;

public abstract class DNSLookUp {

    private static final Logger logger = LogManager.getLogger(DNSLookUp.class);
    private static final Map<String, Record[]> RECORDS = new HashMap<>();

    public static Record[] extract(String domain, int type) throws TextParseException {
        String key = domain + ":" + type;
        if (!RECORDS.containsKey(key)) {
            Lookup lookup = new Lookup(domain, type);
            RECORDS.put(key, lookup.run());
            if(lookup.getResult()!= Lookup.SUCCESSFUL) {
                logger.error("{} {} Failed", domain, Type.string(type));
            }
        }
        return RECORDS.get(key);
    }

    public static <T extends Record> boolean contains(T[] records, String expected, RecordProcessor<T> processor) {
        return Arrays.stream(records)
                .map(processor::process)
                .anyMatch(value -> value.equals(expected));
    }

}
