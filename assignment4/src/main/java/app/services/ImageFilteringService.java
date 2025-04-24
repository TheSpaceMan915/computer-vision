package app.services;

import app.FilterType;
import app.MorphShape;

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
        log.debug("Filter kernel size: '{}'", kernelSize);
        log.info("The normalized filter was applied to the image");
        return blurred;
    }

    /*
     * Apply the Gaussian filter to an image.
     */
    private Mat applyGaussianFilter(Mat image, int kernelSize) {
        Mat blurred = new Mat();
        Imgproc.GaussianBlur(image, blurred, new Size(kernelSize, kernelSize), 0);
        log.debug("Filter kernel size: '{}'", kernelSize);
        log.info("The Gaussian filter was applied to the image");
        return blurred;
    }

    /*
     * Apply the median filter to an image.
     */
    private Mat applyMedianFilter(Mat image, int kernelSize) {
        Mat blurred = new Mat();
        Imgproc.medianBlur(image, blurred, kernelSize);
        log.debug("Filter kernel size: '{}'", kernelSize);
        log.info("The median filter was applied to the image");
        return blurred;
    }

    /*
     * Apply the bilateral filter to an image.
     */
    private Mat applyBilateralFilter(Mat image, int kernelSize) {
        Mat blurred = new Mat();
        Imgproc.bilateralFilter(image, blurred, kernelSize, 25, 25);
        log.debug("Filter kernel size: '{}'", kernelSize);
        log.info("The bilateral filter was applied to the image");
        return blurred;
    }

    /*
    * Map a MorphShape enum to an OpenCV MorphShape enum.
    */
    private int mapMorphShape(MorphShape morphShape) {
        return switch (morphShape) {
            case RECTANGLE -> 0;
            case CROSS -> 1;
            case ELLIPSE -> 2;
        };
    }

    /*
    * Apply erosion to an image using a specific kernel.
    */
    public Optional<Mat> applyErosion(Mat image, int kernelSize, MorphShape morphShape) {
        try {
//            Create a kernel to be used for erosion
            int morphCode = mapMorphShape(morphShape);
            Mat kernel = Imgproc.getStructuringElement(morphCode, new Size(kernelSize, kernelSize));

//            Apply erosion
            Mat eroded = new Mat();
            Imgproc.erode(image, eroded, kernel);
            log.debug("Filter kernel size: '{}'", kernelSize);
            log.info("Erosion was applied to the image");
            return Optional.of(eroded);
        } catch (Exception e) {
            log.error("Failed to apply erosion to the image", e);
            return Optional.empty();
        }
    }

    /*
     * Apply dilation to an image using a specific kernel.
     */
    public Optional<Mat> applyDilation(Mat image, int kernelSize, MorphShape morphShape) {
        try {
//            Create a kernel to be used for dilation
            int morphCode = mapMorphShape(morphShape);
            Mat kernel = Imgproc.getStructuringElement(morphCode, new Size(kernelSize, kernelSize));

//            Apply dilation
            Mat dilated = new Mat();
            Imgproc.dilate(image, dilated, kernel);
            log.debug("Filter kernel size: '{}'", kernelSize);
            log.info("Dilation was applied to the image");
            return Optional.of(dilated);
        } catch (Exception e) {
            log.error("Failed to apply dilation to the image", e);
            return Optional.empty();
        }
    }
}
