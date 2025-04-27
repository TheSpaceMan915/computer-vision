package app;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

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
        log.debug("Loading the image from '{}'", path);
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
        log.debug("Saving the image to '{}'", imagePath);
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
    public Optional<Mat> nullifyChannel(Mat image, Channel channel) {
        try {
//        Find a starting iteration index
            int startIndex = switch (channel) {
                case BLUE -> 0;
                case GREEN -> 1;
                case RED -> 2;
            };

//        Convert an image to a byte array
            int numberBytes = (int) (image.total() * image.elemSize());
            log.debug("Number of bytes: '{}'", numberBytes);
            byte[] bytes = new byte[numberBytes];
            image.get(0, 0, bytes);

//        Nullify the channel
            for (int i = startIndex; i < numberBytes; i += 3) {
                bytes[i] = 0;
            }

//       Convert a byte array to an image
            Mat processedImage = new Mat(image.size(), image.type());
            processedImage.put(0, 0, bytes);
            log.info("The image was successfully nullified");
            return Optional.of(processedImage);
        } catch (Exception e) {
            log.error("Failed to nullify the channel", e);
            return Optional.empty();
        }
    }
}
