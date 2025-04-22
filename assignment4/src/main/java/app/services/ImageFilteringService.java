package app.services;

import app.FilterType;

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
     * Apply a specified filter to an image.
     */
    public Mat applyFilter(Mat image, int kernelSize, FilterType filterType) {
        return switch (filterType) {
            case NORMALIZED -> applyNormalizedFilter(image, kernelSize);
            case GAUSSIAN -> applyGaussianFilter(image, kernelSize);
            case MEDIAN -> applyMedianFilter(image, kernelSize);
            case BILATERAL -> applyBilateralFilter(image, kernelSize);
        };
    }

    /*
     * Apply the normalized filter to an image.
     */
    private Mat applyNormalizedFilter(Mat image, int kernelSize) {
        Mat blurred = new Mat();
        Imgproc.blur(image, blurred, new Size(kernelSize, kernelSize));
        log.info("Filter kernel size: ({}, {})", kernelSize, kernelSize);
        log.info("The normalized filter was applied to the image");
        return blurred;
    }

    /*
     * Apply the Gaussian filter to an image.
     */
    private Mat applyGaussianFilter(Mat image, int kernelSize) {
        Mat blurred = new Mat();
        Imgproc.GaussianBlur(image, blurred, new Size(kernelSize, kernelSize), 0);
        log.info("Filter kernel size: ({}, {})", kernelSize, kernelSize);
        log.info("The Gaussian filter was applied to the image");
        return blurred;
    }

    /*
     * Apply the median filter to an image.
     */
    private Mat applyMedianFilter(Mat image, int kernelSize) {
        Mat blurred = new Mat();
        Imgproc.medianBlur(image, blurred, kernelSize);
        log.info("Filter kernel size: ({}, {})", kernelSize, kernelSize);
        log.info("The median filter was applied to the image");
        return blurred;
    }

    /*
     * Apply the bilateral filter to an image.
     */
    private Mat applyBilateralFilter(Mat image, int kernelSize) {
        Mat blurred = new Mat();
        Imgproc.bilateralFilter(image, blurred, kernelSize, 25, 25);
        log.info("Filter kernel size: '{}'", kernelSize);
        log.info("The bilateral filter was applied to the image");
        return blurred;
    }
}
