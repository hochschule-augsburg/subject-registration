package de.hochschule.augsburg;

import de.hochschule.augsburg.mailing.service.MailingService;
import de.hochschule.augsburg.mailing.utility.MailType;
import de.hochschule.augsburg.mailing.utility.Property;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;


public class MailingTest {
    MailingService service = new MailingService();
    public static String APPLICATION_PROPERTY_PATH = "./src/test/resources/application.properties";
    public static String MAILING_PROPERTY_PATH = "./src/test/resources/mailing.properties";
    private final MailType mailType = MailType.REGISTRATION_STARTS_SOON;
    private final String mailContent = "Anmeldung laeft bald los!";

    @Before
    public void setup() {
        Property.setApplicationPropertyPath(APPLICATION_PROPERTY_PATH);
        Property.setMailingPropertyPath(MAILING_PROPERTY_PATH);
    }

    @Test
    public void testsendEmail() {
        service.sendEmail(mailType, mailContent);
    }
}
