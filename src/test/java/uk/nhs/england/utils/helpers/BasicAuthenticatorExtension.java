package uk.nhs.england.utils.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.net.Authenticator;

/**
 * A JUnit 5 extension that sets the default {@link Authenticator} to {@link BasicAuthenticator} if the system property
 * {@code authType} is set to {@code basic}.
 *
 * {@code username} and {@code password} system properties must also be set.
 *
 * @see BasicAuthenticator
 */
public class BasicAuthenticatorExtension implements BeforeAllCallback {

    private static final Logger logger = LogManager.getLogger(BasicAuthenticatorExtension.class);

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (BasicAuthenticator.requiresBasicAuth()) {
            logger.info("Setting default Authenticator to BasicAuthenticator");
            Authenticator.setDefault(new BasicAuthenticator());
        }
    }

}
