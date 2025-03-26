package app;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

/*
* Provides access to the environment properties
* used in the app.
*/
@Log4j2
public class Config {

    private final Properties properties = new Properties();

    /*
    * Load the environment properties from the environment.properties file.
    */
    public Config() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("environment.properties")) {
            if (input == null) {
                log.warn("Could not find environment.properties file in resources");
                return;
            }
            properties.load(input);
            log.info("environment.properties file was successfully loaded");
        } catch (IOException e) {
            log.error("Failed to load environment.properties file", e);
        }
    }

    /*
    * Return an environment property.
    */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            log.warn("Could not find property '{}'", key);
        }
        return value;
    }
}
