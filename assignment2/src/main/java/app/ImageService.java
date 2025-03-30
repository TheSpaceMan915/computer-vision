package app;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

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
     * Make bytes of a specified channel null.
     */
    public Mat nullifyChannel(Mat image, int channel) {

//        Convert an image to a byte array
        int numberBytes = (int) (image.total() * image.elemSize());
        log.info("Number bytes: {}", numberBytes);
        byte[] bytes = new byte[numberBytes];
        image.get(0, 0, bytes);

        /*
        * Nullify the channel
        * 1st channel = i + 3 - nullify every 3rd value (i = 0)
        * 2nd channel = i + 2 - nullify every 2nd value (i = 0)
        * 3rd channel = i + 3 - nullify every 3rd value (i = 2)
        */
        switch (channel) {
            case 1 -> {
                for (int i = 0; i < numberBytes; i = i + 3) {
                    bytes[i] = 0;
                }
            }
            case 2 -> {
                for (int i = 0; i < numberBytes; i = i + 2) {
                    bytes[i] = 0;
                }
            }
            case 3 -> {
                for (int i = 2; i < numberBytes; i = i + 3) {
                    bytes[i] = 0;
                }
            }
            default -> {
                log.warn("The channel number '{}' is out of range", channel);
                log.warn("The channel number must be between 1 and 3");
                return image;
            }
        }
        image.put(0, 0, bytes);
        log.info("The image was successfully nullified");
        return image;
    }
}
