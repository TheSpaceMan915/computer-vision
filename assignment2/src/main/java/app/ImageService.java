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

    private final Config config = new Config();

    /*
    * Load an image from an absolute path.
    * */
    public Mat loadImage(String imagePath) {
        Path path = Paths.get(imagePath);
        log.info("Loading image from '{}'", path);
        if (!Files.exists(path)) {
            log.warn("Could not find image at '{}'", path);
            return null;
        }
        log.info("The image was successfully loaded");
        return Imgcodecs.imread(path.toString());
    }

    /*
     * Make bytes of a specified channel null.
     * */
    public Mat nullifyChannel(Mat image, int channel) {

//        Convert an image to a byte array
        int numberBytes = (int) (image.total() * image.elemSize());
        log.info("Number bytes: {}", numberBytes);
        byte[] bytes = new byte[numberBytes];
        image.get(0, 0, bytes);

        /*
        * Nullify the channel
        * 1st channel = i + 3 - zero every 3rd value (i = 0)
        * 2nd channel = i + 2 - zero every 2nd value (i = 0)
        * 3rd channel = i + 3 - zero every 3rd value (i = 2)
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

//        Save the processed image
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), config.getProperty(Constants.PROCESSED_IMAGE_NAME));
        Imgcodecs.imwrite(processedImagePath.toString(), image);
        log.info("The processed image was successfully saved");
        return image;
    }
}
