package uk.nhs.england.utils.helpers;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import static uk.nhs.england.utils.helpers.SystemProperties.getAuthType;

public class BasicAuthenticator extends Authenticator {

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(System.getProperty("username"), System.getProperty("password").toCharArray());
    }

    public static boolean requiresBasicAuth() {
        String authType = getAuthType();
        return (authType != null && authType.equalsIgnoreCase("basic"));
    }

}
