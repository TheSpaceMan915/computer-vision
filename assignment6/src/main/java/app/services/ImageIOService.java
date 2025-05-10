package app.services;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/*
 * A service that handles reading and writing of images.
 */
@Log4j2
public class ImageIOService {

    /*
     * Read an image from a specified path.
     */
    public Optional<Mat> readImage(String imagePath) {
        Path path = Paths.get(imagePath);
        log.debug("Loading the image from '{}'", path);
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
        log.debug("Saving the image to '{}'", imagePath);
        boolean isSaved = Imgcodecs.imwrite(imagePath, image);
        if (!isSaved) {
            log.warn("Could not save the image at '{}'", imagePath);
            return isSaved;
        }
        log.info("The image was successfully saved");
        return isSaved;
    }
}
