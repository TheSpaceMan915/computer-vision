package app;

import lombok.extern.log4j.Log4j2;

import nu.pattern.OpenCV;

import org.opencv.core.Core;

/*
 * Locally loads OpenCV library.
 */
@Log4j2
public class OpenCVLoader {

    /*
    * Load the appropriate version of OpenCV in the project.
    */
    public OpenCVLoader() {
        try {
            OpenCV.loadLocally();
            log.info("OpenCV was successfully loaded");
            log.info("OpenCV version '{}'", Core.getVersionString());
            log.info("OS '{}'", System.getProperty("os.name") );
        } catch (Exception exception) {
            log.error("Failed to load OpenCV", exception);
        }
    }
}
