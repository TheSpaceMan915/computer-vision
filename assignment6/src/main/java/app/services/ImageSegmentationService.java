package app.services;

import app.ThresholdType;
import app.ThresholdResult;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.Optional;
import java.util.Random;

/*
* A service that handles image segmentation functionality.
*/
@Log4j2
public class ImageSegmentationService {

    private final ImageTransformationService imageTransformationService = new ImageTransformationService();

    /*
    * Generate a random Scalar.
    */
    private Scalar generateRandomScalar() {
        Random random = new Random();
        int v0 = random.nextInt(256);
        int v1 = random.nextInt(256);
        int v2 = random.nextInt(256);
        return new Scalar(v0, v1, v2);
    }

    /*
    * Apply Flood Fill algorithm to an image.
    */
    public Optional<Mat> applyFloodFill(Mat image,
                                        Point start,
                                        Scalar colour,
                                        Scalar lowerDiff,
                                        Scalar upperDiff) {
        if (colour == null && lowerDiff == null && upperDiff == null) {
            colour = generateRandomScalar();
            lowerDiff = generateRandomScalar();
            upperDiff = generateRandomScalar();
        }
        Mat mask = new Mat();
        Rect filledRegion = new Rect();
        try {
            Imgproc.floodFill(
                    image,
                    mask,
                    start,
                    colour,
                    filledRegion,
                    lowerDiff,
                    upperDiff,
                    Imgproc.FLOODFILL_FIXED_RANGE);
            log.info("Flood Fill algorithm was applied to the image");
            return Optional.of(image);
        } catch (Exception e) {
            log.error("Failed to apply Flood Fill algorithm to the image", e);
            return Optional.empty();
        }
    }

    /*
    * Build a downscaling pyramid from an image.
    */
    public Optional<Mat> buildDownscalingPyramid(Mat image, int levelNumber) {
        try {
            Mat current = image.clone();
            for (int i = 0; i < levelNumber; i++) {
                Mat downscaled = new Mat();
                Imgproc.pyrDown(current, downscaled);
                log.debug("Current image size: '{}'", current.size());
                log.debug("Downscaled image size: '{}'", downscaled.size());
                current = downscaled;
            }
            log.info("A downscaling pyramid with '{}' levels was built", levelNumber);
            return Optional.of(current);
        } catch (Exception e) {
            log.error("Failed to build a downscaling pyramid", e);
            return Optional.empty();
        }
    }

    /*
     * Build an upscaling pyramid from an image.
     */
    public Optional<Mat> buildUpscalingPyramid(Mat image, int levelNumber) {
        try {
            Mat current = image.clone();
            for (int i = 0; i < levelNumber; i++) {
                Mat upscaled = new Mat();
                Imgproc.pyrUp(current, upscaled);
                log.debug("Current image size: '{}'", current.size());
                log.debug("Upscaled image size: '{}'", upscaled.size());
                current = upscaled;
            }
            log.info("An upscaling pyramid with '{}' levels was built", levelNumber);
            return Optional.of(current);
        } catch (Exception e) {
            log.error("Failed to build an upscaling pyramid", e);
            return Optional.empty();
        }
    }

    /*
     * Reconstruct an image using a downscaling and an upscaling image pyramid.
     */
    public Optional<Mat> reconstructFromPyramid(Mat image, int levelNumber) {
        try {
//            Reconstruct the original image
            Optional<Mat> optDownscaled = buildDownscalingPyramid(image, levelNumber);
            if (optDownscaled.isEmpty()) return Optional.empty();

            Optional<Mat> optUpscaled = buildUpscalingPyramid(optDownscaled.get(), levelNumber);
            if (optUpscaled.isEmpty()) return Optional.empty();
            Mat upscaled = optUpscaled.get();

//                Get the image sizes
            Size originalImageSize = image.size();
            Size upscaledImageSize = upscaled.size();

//                Check if the image sizes match
            if (!upscaledImageSize.equals(originalImageSize)) {
//        Restore the upscaled image size
                upscaled = imageTransformationService
                        .resize(upscaled, originalImageSize)
                        .orElseThrow();
                log.debug("The upscaled image was resized to the original image size");
            }
            return Optional.of(upscaled);
        } catch (Exception e) {
            log.error("Failed to reconstruct the image from the pyramid", e);
            return Optional.empty();
        }
    }

    /*
     * Compute the difference between two images.
     */
    public Optional<Mat> subtract(Mat image1, Mat image2) {
        try {
            Mat difference = new Mat();
            Core.subtract(image1, image2, difference);
            log.info("The difference between the two images was computed");
            return Optional.of(difference);
        } catch (Exception e) {
            log.error("Failed to compute the difference between the two images", e);
            return Optional.empty();
        }
    }

    /*
    * Apply threshold to an image.
    */
    public Optional<ThresholdResult> applyThreshold(
            Mat image,
            double threshold,
            double maxValue,
            ThresholdType thresholdType,
            boolean useOtsu) {
        try {
            Mat thresholded = new Mat();
            int thresholdCode = switch (thresholdType) {
                case BINARY -> 0;
                case BINARY_INV -> 1;
                case TRUNC -> 2;
                case TOZERO -> 3;
                case TOZERO_INV -> 4;
            };
            if (useOtsu) {
                threshold = Imgproc.threshold(
                        image,
                        thresholded,
                        threshold,
                        maxValue,
                        thresholdCode | Imgproc.THRESH_OTSU);
                log.debug("Otsu threshold value: '{}'", threshold);
                log.info("Threshold with Otsu was applied to the image");
                return Optional.of(new ThresholdResult(threshold, thresholded));
            }
            Imgproc.threshold(
                    image,
                    thresholded,
                    threshold,
                    maxValue,
                    thresholdCode);
            log.info("Threshold was applied to the image");
            return Optional.of(new ThresholdResult(threshold, thresholded));
        } catch (Exception e) {
            log.error("Failed to apply threshold to the image", e);
            return Optional.empty();
        }
    }
}
