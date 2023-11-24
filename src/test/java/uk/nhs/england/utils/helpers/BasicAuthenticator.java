package uk.nhs.england.utils.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import static uk.nhs.england.utils.helpers.SystemProperties.getAuthType;

public class BasicAuthenticator extends Authenticator {

    private static final Logger logger = LogManager.getLogger(BasicAuthenticator.class);


    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(System.getProperty("username"), System.getProperty("password").toCharArray());
    }

    public static boolean usingBasicAuth() {
        String authType = getAuthType();
        return (authType != null && authType.equalsIgnoreCase("basic"));
    }

}
