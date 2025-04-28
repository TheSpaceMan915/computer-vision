package app.services;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Optional;
import java.util.Random;

/*
* A service that handles image segmentation functionality.
*/
@Log4j2
public class ImageSegmentationService {

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
}
