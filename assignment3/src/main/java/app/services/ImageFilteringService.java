package app.services;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Optional;

/*
 * A service that handles image filtering functionality.
 */
@Log4j2
public class ImageFilteringService {

    /*
     * Convert a BGR image to a grayscale image.
     */
    public Optional<Mat> convertToGrayscale(Mat image) {
        try {
            Mat grayscale = new Mat();
            Imgproc.cvtColor(image, grayscale, Imgproc.COLOR_BGR2GRAY);
            log.info("The image was converted to grayscale");
            return Optional.of(grayscale);
        } catch (Exception e) {
            log.error("Failed to convert the image to grayscale", e);
            return Optional.empty();
        }
    }

    /*
     * Apply the Gaussian blur to reduce image noise.
     */
    public Optional<Mat> applyGaussianBlur(Mat image, int width, int height) {
        try {
            Mat blurred = new Mat();
            Imgproc.GaussianBlur(image, blurred, new Size(width, height), 0);
            log.debug("Gaussian kernel size: ({}, {})", width, height);
            log.info("The Gaussian blur was applied to the image");
            return Optional.of(blurred);
        } catch (Exception e) {
            log.error("Failed to apply Gaussian blur to the image", e);
            return Optional.empty();
        }
    }
}
