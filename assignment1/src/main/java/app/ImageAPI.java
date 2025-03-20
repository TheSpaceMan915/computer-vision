package app;

import lombok.extern.log4j.Log4j2;

import nu.pattern.OpenCV;

import org.opencv.core.Core;

@Log4j2
public class ImageAPI {

//    Initialise the library
    public ImageAPI() {
        try {
            OpenCV.loadLocally();
            log.info("OpenCV loaded successfully");
            log.info("OpenCV version '{}'", Core.getVersionString());
            log.info("OS '{}'", System.getProperty("os.name") );
        }
        catch (Exception exception) {
            log.error("OpenCV initialisation failed", exception);
        }
    }
}
