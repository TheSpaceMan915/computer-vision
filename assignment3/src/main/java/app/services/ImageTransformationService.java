package app.services;

import app.Axis;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;

/*
 * A service that provides image transformation functionality.
 */
@Log4j2
public class ImageTransformationService {

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

    /*
     * Translate an image by the specified number of pixels
     * along x and y axes.
     */
    public Mat translate(Mat image, int dx, int dy) {

//        Define the translation matrix
        Mat transMat = Mat.eye(2, 3, CvType.CV_32F);
        transMat.put(0, 2, dx);
        transMat.put(1, 2, dy);

//        Apply the translation
        Mat translated = new Mat();
        Imgproc.warpAffine(image, translated, transMat, image.size());
        log.info("The image was translated by ({}, {}) pixels", dx, dy);
        return translated;
    }
}
