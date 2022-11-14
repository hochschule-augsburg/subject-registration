package de.hochschule.augsburg.mailing.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import camundajar.impl.scala.sys.Prop;

public final class Property {
    public final static String APPLICATION_PROPERTY_PATH = "backend/src/main/resources/application.properties";
    public final static String MAILING_PROPERTY_PATH = "backend/src/main/resources/mailing.properties";

    public static Properties getMailingProperty() {
        return getProperties(MAILING_PROPERTY_PATH);
    }

    public static Properties getApplicationProperty() {
        return getProperties(APPLICATION_PROPERTY_PATH);
    }

    private static Properties getProperties(String path) {
        try (InputStream input = new FileInputStream(path)) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
