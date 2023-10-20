package uk.nhs.england.dnslookups;

import java.io.BufferedReader;
import java.io.InputStreamReader;

// This is not formally part of the test suite, but it is a useful utility for debugging.
// It is a simple command line tool that can be used to extract the raw DNS data for a domain in a readable format.
public class DNSLookUpFullRawExtractor {

    public static void main(String[] args) throws Exception {

        String domain = "digital.nhs.uk";
        String domainFiles = "files.digital.nhs.uk";

// ----------------------------------------------
        System.out.println("--------------------------------------------------");
        // A
        Process processA = Runtime.getRuntime().exec("dig +short " + domain + " A");
        BufferedReader readerA = new BufferedReader(new InputStreamReader(processA.getInputStream()));

        String lineA;
        while ((lineA = readerA.readLine()) != null) {
            System.out.println("DIG A: "+lineA);
        }
        readerA.close();
// ----------------------------------------------
        System.out.println("--------------------------------------------------");
        // CNAME
        Process processCNAME = Runtime.getRuntime().exec("dig +short " + domainFiles + " CNAME");
        BufferedReader readerCNAME = new BufferedReader(new InputStreamReader(processCNAME.getInputStream()));

        String lineCNAME;
        while ((lineCNAME = readerCNAME.readLine()) != null) {
            System.out.println("DIG CNAME: "+lineCNAME);
        }
        readerCNAME.close();
// ----------------------------------------------
        System.out.println("--------------------------------------------------");
        // NS
        Process processNS = Runtime.getRuntime().exec("dig +short " + domain + " NS");
        BufferedReader readerNS = new BufferedReader(new InputStreamReader(processNS.getInputStream()));

        String lineNS;
        while ((lineNS = readerNS.readLine()) != null) {
            System.out.println("DIG NS: "+lineNS);
        }
        readerNS.close();
// ----------------------------------------------
        System.out.println("--------------------------------------------------");
        // SOA
        Process processSOA = Runtime.getRuntime().exec("dig +short " + domain + " SOA");
        BufferedReader readerSOA = new BufferedReader(new InputStreamReader(processSOA.getInputStream()));

        String lineSOA;
        while ((lineSOA = readerSOA.readLine()) != null) {
            System.out.println("DIG SOA: "+lineSOA);
        }
        readerSOA.close();
// ----------------------------------------------
        System.out.println("--------------------------------------------------");
        // TXT
        Process processTXT = Runtime.getRuntime().exec("dig +short " + domain + " TXT");
        BufferedReader readerTXT = new BufferedReader(new InputStreamReader(processTXT.getInputStream()));

        String lineTXT;
        while ((lineTXT = readerTXT.readLine()) != null) {
            System.out.println("DIG TXT: "+lineTXT);
        }
        readerTXT.close();
    }

}
