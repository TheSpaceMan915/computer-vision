package app.services;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/*
 * A service that handles image filtering functionality.
 */
@Log4j2
public class ImageFilteringService {

    /*
     * Convert a BGR image to a grayscale image.
     */
    public Mat convertToGrayscale(Mat image) {
        Mat grayscale = new Mat();
        Imgproc.cvtColor(image, grayscale, Imgproc.COLOR_BGR2GRAY);
        log.info("The image was converted to grayscale");
        return grayscale;
    }

    /*
     * Apply the Gaussian blur to reduce image noise.
     */
    public Mat applyGaussianBlur(Mat image, int width, int height) {
        Mat blurred = new Mat();
        Imgproc.GaussianBlur(image, blurred, new Size(width, height), 0);
        log.info("Gaussian kernel size: ({}, {})", width, height);
        log.info("The Gaussian blur was applied to the image");
        return blurred;
    }
}
