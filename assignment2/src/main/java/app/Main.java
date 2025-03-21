package app;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

@Log4j2
public class Main {

    public static void main( String[] args ) {
        log.info("This is a computer vision API");
//        TODO: Initialise OpenCV
        Mat mat = Imgcodecs.imread("assignment2/src/main/resources/billie_jean.jpg");
    }
}
