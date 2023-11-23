package uk.nhs.england.dnslookups;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;
import uk.nhs.england.tags.Production;
import uk.nhs.england.tags.Uat;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DNSLookUpTests {

    private static final Logger logger = LogManager.getLogger(DNSLookUpTests.class);

    @ParameterizedTest @Production
    @CsvFileSource(resources = "/dns/produciton.csv", useHeadersInDisplayName = true)
    public void testProductionDns(String record, String domain, String expected) throws TextParseException {
        testDns(record, domain, expected);
    }

    @ParameterizedTest @Uat
    @CsvFileSource(resources = "/dns/uat.csv", useHeadersInDisplayName = true)
    public void testUatDns(String record, String domain, String expected) throws TextParseException {
        testDns(record, domain, expected);
    }

    private void testDns(String record, String domain, String expected) throws TextParseException {
        switch (record) {
            case "A":
                Record[] aRecords = DNSLookUpA.extract(domain, Type.A);
                assertTrue(DNSLookUpA.contains(aRecords, expected));
                break;
            case "CNAME":
                Record[] cnameRecords = DNSLookUpCNAME.extract(domain, Type.CNAME);
                assertTrue(DNSLookUpCNAME.contains(cnameRecords, expected));
                break;
            case "NS":
                Record[] nsRecords = DNSLookUpNS.extract(domain, Type.NS);
                assertTrue(DNSLookUpNS.contains(nsRecords, expected));
                break;
            case "SOA":
                Record[] soaRecords = DNSLookUpSOA.extract(domain, Type.SOA);
                assertTrue(DNSLookUpSOA.contains(soaRecords, expected));
                break;
            case "TXT":
                Record[] txtRecords = DNSLookUpTXT.extract(domain, Type.TXT);
                assertTrue(DNSLookUpTXT.contains(txtRecords, expected));
                break;
            default:
                throw new IllegalStateException("Unexpected Record Type: " + record);
        }

        logger.info("{} {} returned the expected value: {}", domain, record, expected);
    }

}
