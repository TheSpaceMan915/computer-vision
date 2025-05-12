package app.services;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.Optional;

/*
 * A service that handles edge detection using various operators.
 */
@Log4j2
public class ImageEdgeDetectionService {

    /*
     * Apply the Sobel operator to detect edges.
     */
    public Optional<Mat> applySobelOperator(Mat image, int ddepth, int kernelSize) {
        try {
//        Apply the Sobel operator in X and Y direction
            Mat gradX = new Mat();
            Mat gradY = new Mat();
            Imgproc.Sobel(image, gradX, ddepth, 1, 0, kernelSize);
            Imgproc.Sobel(image, gradY, ddepth, 0, 1, kernelSize);

//        Convert gradients to absolute values
            Mat absGradX = new Mat();
            Mat absGradY = new Mat();
            Core.convertScaleAbs(gradX, absGradX);
            Core.convertScaleAbs(gradY, absGradY);

//        Combine the gradients
            Mat edgeImage = new Mat();
            Core.addWeighted(absGradX, 0.5, absGradY, 0.5, 0, edgeImage);
            log.info("The Sobel operator was applied to the image");
            return Optional.of(edgeImage);
        } catch (Exception e) {
            log.error("Failed to apply the Sobel operator to the image", e);
            return Optional.empty();
        }
    }

    /*
     * Apply the Laplacian operator to detect edges.
     */
    public Optional<Mat> applyLaplacianOperator(Mat image, int ddepth, int kernelSize) {
        try {
//        Apply the Laplacian operator
            Mat laplacian = new Mat();
            Imgproc.Laplacian(image, laplacian, ddepth, kernelSize);

//        Convert gradients to absolute values
            Mat abs = new Mat();
            Core.convertScaleAbs(laplacian, abs);
            log.info("The Laplacian operator was applied to the image");
            return Optional.of(abs);
        } catch (Exception e) {
            log.error("Failed to apply the Laplacian operator to the image", e);
            return Optional.empty();
        }
    }

    /*
     * Apply Canny algorithm to an image.
     */
    public Optional<Mat> applyCanny(Mat image, double threshold1, double threshold2) {
        try {
            Mat edges = new Mat();
            Imgproc.Canny(image, edges, threshold1, threshold2);
            log.info("Canny algorithm was applied to the image");
            return Optional.of(edges);
        } catch (Exception e) {
            log.error("Failed to apply Canny algorithm to the image", e);
            return Optional.empty();
        }
    }
}
