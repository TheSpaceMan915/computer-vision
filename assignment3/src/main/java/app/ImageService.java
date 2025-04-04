package app;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
* Provides functionality to process images.
*/
@Log4j2
public class ImageService {

    /*
    * Read an image from a specified path.
    */
    public Mat readImage(String imagePath) {
        Path path = Paths.get(imagePath);
        log.info("Loading the image from '{}'", path);
        if (!Files.exists(path)) {
            log.warn("Could not find the image at '{}'", path);
            return null;
        }
        log.info("The image was successfully loaded");
        return Imgcodecs.imread(path.toString());
    }

    /*
    * Save an image to a specified path.
    */
    public boolean writeImage(Mat image, String imagePath) {
        log.info("Saving the image to '{}'", imagePath);
        boolean isSaved = Imgcodecs.imwrite(imagePath, image);
        if (!isSaved) {
            log.warn("Could not save the image at '{}'", imagePath);
            return false;
        }
        log.info("The image was successfully saved");
        return isSaved;
    }

    /*
     * Convert a BGR image to a grayscale image.
     */
    public Mat BGRToGrayscale(Mat image) {
        Mat grayscale = new Mat();
        Imgproc.cvtColor(image, grayscale, Imgproc.COLOR_BGR2GRAY);
        log.info("The image was converted to grayscale");
        return grayscale;
    }

    /*
     * Apply a Gaussian blur to reduce image noise.
     */
    public Mat applyGaussianBlur(Mat image, Size kernelSize) {
        Mat blurred = new Mat();
        Imgproc.GaussianBlur(image, blurred, kernelSize, 0);
        log.info("A Gaussian blur was applied to the image");
        return blurred;
    }

    /*
     * Apply the Sobel operator to an image to detect edges.
     */
    public Mat applySobelOperator(Mat image, int ddepth, int kernelSize) {

//        Apply Sobel operator in X and Y direction
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
        return edgeImage;
    }
}
