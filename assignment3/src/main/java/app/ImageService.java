package app;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/*
* A service that provides image processing functionality.
*/
@Log4j2
public class ImageService {

    /*
     * Read an image from the specified path.
     */
    public Optional<Mat> readImage(String imagePath) {
        Path path = Paths.get(imagePath);
        log.info("Loading the image from '{}'", path);
        if (!Files.exists(path)) {
            log.warn("Could not find the image at '{}'", path);
            return Optional.empty();
        }
        log.info("The image was successfully loaded");
        Mat image = Imgcodecs.imread(imagePath);
        return Optional.of(image);
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
    public Mat convertToGrayscale(Mat image) {
        Mat grayscale = new Mat();
        Imgproc.cvtColor(image, grayscale, Imgproc.COLOR_BGR2GRAY);
        log.info("The image was converted to grayscale");
        return grayscale;
    }

    /*
     * Apply a Gaussian blur to reduce image noise.
     */
    public Mat applyGaussianBlur(Mat image, int width, int height) {
        Mat blurred = new Mat();
        Imgproc.GaussianBlur(image, blurred, new Size(width, height), 0);
        log.info("A Gaussian blur was applied to the image");
        return blurred;
    }

    /*
     * Apply the Sobel operator to detect edges.
     */
    public Mat applySobelOperator(Mat image, int ddepth, int kernelSize) {

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
        return edgeImage;
    }

    /*
     * Apply the Laplacian operator to detect edges.
     */
    public Mat applyLaplacianOperator(Mat image, int ddepth, int kernelSize) {

//        Apply the Laplacian operator
        Mat laplacian = new Mat();
        Imgproc.Laplacian(image, laplacian, ddepth, kernelSize);

//        Convert gradients to absolute values
        Mat abs = new Mat();
        Core.convertScaleAbs(laplacian, abs);
        log.info("The Laplacian operator was applied to the image");
        return abs;
    }

    /*
     * Flip an image around an axis or both axes.
     */
    public Mat flip(Mat image, Axis axis) {
        int flipCode = switch (axis) {
            case X -> 0;
            case Y -> 1;
            case BOTH -> -1;
        };
        Mat flipped = new Mat();
        Core.flip(image, flipped, flipCode);
        log.info("The image was flipped");
        return flipped;
    }

    /*
     * Repeat an image along the axes.
     */
    public Mat repeat(Mat image, int ny, int nx) {
        Mat repeated = new Mat();
        Core.repeat(image, ny, nx, repeated);
        log.info("The image was repeated '{}' times along y-axis and '{}' along x-axis", ny, nx);
        return repeated;
    }

    /*
     * Check if there are enough images in the list
     * to apply concatenation.
     */
    private boolean hasEnoughImages(List<Mat> images) {
        if (images.size() < 2) {
            log.warn("There are not enough images in the list");
            log.warn("You should have at least 2 images for concatenation");
            return false;
        }
        log.info("The list has enough images for concatenation");
        return true;
    }

    /*
     * Check if the images have the same height.
     */
    private boolean haveSameHeight(List<Mat> images) {
        int height = images.getFirst().height();
        log.info("Image height is '{}' pixels", height);

        for (int i = 1; i < images.size(); i++) {
            Mat currentImage = images.get(i);
            if (currentImage.height() != height) {
                log.warn("Could not apply horizontal concatenation to the given images");
                log.warn("The images must have the same height");
                return false;
            }
        }
        log.info("The images have the same height");
        return true;
    }

    /*
     * Check if the images have the same width.
     */
    private boolean haveSameWidth(List<Mat> images) {
        int width = images.getFirst().width();
        log.info("Image width is '{}' pixels", width);

        for (int i = 1; i < images.size(); i++) {
            Mat currentImage = images.get(i);
            if (currentImage.width() != width) {
                log.warn("Could not apply vertical concatenation to the given images");
                log.warn("The images must have the same width");
                return false;
            }
        }
        log.info("The images have the same width");
        return true;
    }

    /*
     * Apply horizontal concatenation to the given images.
     */
    public Optional<Mat> hconcat(List<Mat> images) {
        if (!hasEnoughImages(images)) {
            return Optional.empty();
        }
        if (!haveSameHeight(images)) {
            return Optional.empty();
        }
        Mat concatenated = new Mat();
        Core.hconcat(images, concatenated);
        log.info("Horizontal concatenation was applied to the images");
        return Optional.of(concatenated);
    }

    /*
     * Apply vertical concatenation to the given images.
     */
    public Optional<Mat> vconcat(List<Mat> images) {
        if (!hasEnoughImages(images)) {
            return Optional.empty();
        }
        if (!haveSameWidth(images)) {
            return Optional.empty();
        }
        Mat concatenated = new Mat();
        Core.vconcat(images, concatenated);
        log.info("Vertical concatenation was applied to the images");
        return Optional.of(concatenated);
    }

    /*
     * Resize an image down or up to the specified size.
     */
    public Mat resize(Mat image, int width, int height) {
        Mat resized = new Mat();
        Imgproc.resize(image, resized, new Size(width, height));
        log.info("The image was resized from '{}' to '{}'", image.size(), resized.size());
        return resized;
    }

    /*
    * Rotate an image by the specified number of degrees.
    * If crop value is true, the image will be cropped and rotated.
    * Otherwise, the image will not be cropped, and the canvas
    * will be expanded to fit all content.
    */
    public Mat rotate(Mat image, double angle, boolean crop) {

//        Get the centre of rotation
        Point centre = new Point(image.width() / 2.0, image.height() / 2.0);

//        Get original canvas size
        Size origCanvasSize = image.size();
        log.info("Original canvas size: '{}'", origCanvasSize);

//        Get rotation matrix
        Mat rotMat = Imgproc.getRotationMatrix2D(centre, angle, 1.0);

        Mat rotated = new Mat();
        if (crop) {
//            Apply the rotation keeping the original size of the canvas
            Imgproc.warpAffine(image, rotated, rotMat, origCanvasSize);
            log.info("The original size of the canvas was kept");
            log.info("The image was rotated");
            return rotated;
        }
//        Canvas resizing
//        Define original image corner coordinates
        Point[] points = new Point[] {
                new Point(0, 0),
                new Point(origCanvasSize.width, 0),
                new Point(0, origCanvasSize.height),
                new Point(origCanvasSize.width, origCanvasSize.height)
        };
        Mat corners = new MatOfPoint2f(points);
        log.info("Original image corner coordinates: '{}'", Arrays.toString(points));

//        Compute new image corner coordinates
        MatOfPoint2f transformed = new MatOfPoint2f();
        Core.transform(corners, transformed, rotMat);
        log.info("New image corner coordinates: '{}'", Arrays.toString(transformed.toArray()));

//        Compute the smallest box that fully contains the rotated image
        Rect bbox = Imgproc.boundingRect(new MatOfPoint(transformed.toArray()));

//        Get new canvas size
        Size newCanvasSize = bbox.size();
        log.info("The canvas was resized");
        log.info("New canvas size: '{}'", newCanvasSize);

//        Adjust the transform to centre the image inside the bbox
        rotMat.put(0, 2, rotMat.get(0, 2)[0] + bbox.width/2 - centre.x);
        rotMat.put(1, 2, rotMat.get(1, 2)[0] + bbox.height/2 - centre.y);

//            Apply the adjusted rotation with the new canvas size
        Imgproc.warpAffine(image, rotated, rotMat, newCanvasSize);
        log.info("The image was rotated");
        return rotated;
    }
}