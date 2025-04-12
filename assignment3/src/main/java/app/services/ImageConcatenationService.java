package app.services;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.List;
import java.util.Optional;

/*
 * A service that provides image concatenation functionality.
 */
@Log4j2
public class ImageConcatenationService {

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
     * Check if the images have the same height or width.
     */
    private boolean haveSameDimension(List<Mat> images, boolean checkHeight) {
        int dimension = checkHeight ? images.getFirst().height() : images.getFirst().width();
        String dimensionName = checkHeight ? "height" : "width";

        for (Mat image : images) {
            if ((checkHeight ? image.height() : image.width()) != dimension) {
                log.warn("The images must have the same {} to proceed", dimensionName);
                return false;
            }
        }
        log.info("The images have the same {}", dimensionName);
        return true;
    }

    /*
     * Apply horizontal concatenation to the given images.
     */
    public Optional<Mat> hconcat(List<Mat> images) {
        if (!hasEnoughImages(images)) {
            return Optional.empty();
        }
        if (!haveSameDimension(images, true)) {
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
        if (!haveSameDimension(images, false)) {
            return Optional.empty();
        }
        Mat concatenated = new Mat();
        Core.vconcat(images, concatenated);
        log.info("Vertical concatenation was applied to the images");
        return Optional.of(concatenated);
    }
}
