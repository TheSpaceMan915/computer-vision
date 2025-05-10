package app.services;

import app.MorphShape;
import app.ThresholdResult;
import app.ThresholdType;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.nio.file.Paths;
import java.util.*;

/*
 * A service that handles object detection functionality.
 */
@Log4j2
public class ObjectDetectionService {

    private final ImageFilteringService imageFilteringService = new ImageFilteringService();

    private final ImageIOService imageIOService = new ImageIOService();

    private final ImageSegmentationService imageSegmentationService = new ImageSegmentationService();

    private final ImageTransformationService imageTransformationService = new ImageTransformationService();

    /*
    * Count detected rectangles.
    */
    private void countRectangles(List<Mat> rectangles) {
        if (rectangles.isEmpty()) {
            log.debug("There are no rectangles on the image");
            return;
        }
        log.debug("There are '{}' rectangles on the image", rectangles.size());
    }

    /*
     * Detect rectangular regions on an image.
     */
    public Optional<List<Mat>> detectRectangles(Mat image,
                                                Size targetSize,
                                                String debugDirPath) {
        try {
            final double TOLERANCE = 0.15;
            final int KERNEL_SIZE = 3;

            List<Mat> rectangles = new ArrayList<>();

//            Convert the image to grayscale
            Optional<Mat> optGrayscale = imageFilteringService.convertToGrayscale(image);
            if (optGrayscale.isEmpty()) return Optional.empty();
            Mat grayscale = optGrayscale.get();
            imageIOService.writeImage(grayscale, Paths.get(debugDirPath, "1. grayscale.jpg").toString());

//            Denoise the image
            Mat denoised = new Mat();
            Photo.fastNlMeansDenoising(grayscale, denoised);
            imageIOService.writeImage(denoised, Paths.get(debugDirPath, "2. denoised.jpg").toString());

//            Enhance contrast using histogram equalization
            Mat histEq = new Mat();
            Imgproc.equalizeHist(denoised, histEq);
            imageIOService.writeImage(histEq, Paths.get(debugDirPath, "3. hist_equalized.jpg").toString());

//            Apply erosion
            Optional<Mat> optEroded = imageFilteringService.applyErosion(
                    histEq,
                    KERNEL_SIZE,
                    MorphShape.RECTANGLE);
            if (optEroded.isEmpty()) return Optional.empty();
            Mat eroded = optEroded.get();
            imageIOService.writeImage(eroded, Paths.get(debugDirPath, "4. eroded.jpg").toString());

//            Apply dilation
            Optional<Mat> optDilated = imageFilteringService.applyDilation(
                    eroded,
                    KERNEL_SIZE,
                    MorphShape.RECTANGLE);
            if (optDilated.isEmpty()) return Optional.empty();
            Mat dilated = optDilated.get();
            imageIOService.writeImage(dilated, Paths.get(debugDirPath, "5. dilated.jpg").toString());

//            Subtract the background
            Optional<Mat> optSubtracted = imageSegmentationService.subtract(histEq, dilated);
            if (optSubtracted.isEmpty()) return Optional.empty();
            Mat subtracted = optSubtracted.get();
            imageIOService.writeImage(subtracted, Paths.get(debugDirPath, "6. subtracted.jpg").toString());

//            Use thresholding to binarize the image
            Optional<ThresholdResult> optThresholdResult
                    = imageSegmentationService.applyThreshold(
                    subtracted,
                    50,
                    255,
                    ThresholdType.BINARY,
                    true);
            if (optThresholdResult.isEmpty()) return Optional.empty();
            ThresholdResult thresholdResult = optThresholdResult.get();
            double threshold = thresholdResult.threshold();
            Mat thresholded = thresholdResult.thresholded();
            imageIOService.writeImage(thresholded, Paths.get(debugDirPath, "7. thresholded.jpg").toString());

//            Apply Canny edge detection
            Mat edges = new Mat();
            Imgproc.Canny(thresholded, edges, threshold, threshold * 3);
            imageIOService.writeImage(edges, Paths.get(debugDirPath, "8. edges.jpg").toString());

//            Apply dilation
            optDilated = imageFilteringService.applyDilation(
                    edges,
                    KERNEL_SIZE,
                    MorphShape.RECTANGLE);
            if (optDilated.isEmpty()) return Optional.empty();
            dilated = optDilated.get();
            imageIOService.writeImage(dilated, Paths.get(debugDirPath, "9. dilated_edges.jpg").toString());

//            Find all connected contours
            List<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(
                    dilated,
                    contours,
                    new Mat(),
                    Imgproc.RETR_TREE,
                    Imgproc.CHAIN_APPROX_SIMPLE);
            contours.sort(Collections.reverseOrder(Comparator.comparing(Imgproc::contourArea)));

//            Filter up to 10 contours by their size
            int index = 0;
            for (MatOfPoint contour : contours.subList(0, Math.min(10, contours.size()))) {
                log.debug("Detected contour #{}", ++index);
                MatOfPoint2f contour2f = new MatOfPoint2f();
                contour.convertTo(contour2f, CvType.CV_32FC2);
                double arcLength = Imgproc.arcLength(contour2f, true);

//                Approximate each contour to 4-point regions
                MatOfPoint2f approx2f = new MatOfPoint2f();
                Imgproc.approxPolyDP(contour2f, approx2f, 0.03 * arcLength, true);

                MatOfPoint approx = new MatOfPoint();
                approx2f.convertTo(approx, CvType.CV_32S);

//                Extract a rectangle
                Rect rect = Imgproc.boundingRect(approx);

//                Compare the detected rectangle size to the expected target size
                double widthRatio = rect.width / targetSize.width;
                double heightRatio = rect.height / targetSize.height;
                log.debug("Contour bounding rectangle: '{}x{}'", rect.width, rect.height);
                log.debug("Ratio width/height: '{}/{}'", String.format("%.2f", widthRatio), String.format("%.2f", heightRatio));

//                Skip the detected rectangle if it is too wide or too long
                if (Math.abs(1 - widthRatio) > TOLERANCE || Math.abs(1 - heightRatio) > TOLERANCE) {
                    log.debug("Skipped a rectangle due to size mismatch");
                    continue;
                }

//                Crop and resize the detected rectangle
                Mat submat = image.submat(rect);
                Optional<Mat> optResized = imageTransformationService.resize(submat, targetSize);
                if (optResized.isEmpty()) return Optional.empty();

//                Save the detected rectangle
                Mat resized = optResized.get();
                rectangles.add(resized);
            }
            countRectangles(rectangles);
            log.info("Rectangle detection was completed");
            return Optional.of(rectangles);
        } catch (Exception e) {
            log.error("Failed to detect rectangles on the image", e);
            return Optional.empty();
        }
    }
}
