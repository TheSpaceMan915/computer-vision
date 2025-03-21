package app;

import lombok.extern.log4j.Log4j2;

import nu.pattern.OpenCV;

import org.opencv.core.Core;

@Log4j2
public class OpenCVLoader {

//    Initialise the library
    public OpenCVLoader() {
        try {
            OpenCV.loadLocally();
            log.info("OpenCV was loaded successfully");
            log.info("OpenCV version '{}'", Core.getVersionString());
            log.info("OS '{}'", System.getProperty("os.name") );
        }
        catch (Exception exception) {
            log.error("OpenCV loading failed", exception);
        }
    }

    public static void main(String[] args) {
        OpenCVLoader loader = new OpenCVLoader();
    }
}
