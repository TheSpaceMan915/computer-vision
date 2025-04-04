package app;

import org.junit.jupiter.api.Test;

import org.opencv.core.Mat;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageServiceTest {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final Config config = new Config();

    private final ImageService imageService = new ImageService();

    @Test
    void testReadImage() {
        Path origImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), config.getProperty(Constants.ORIG_IMAGE_NAME));
        Mat image = imageService.readImage(origImagePath.toString());
    }

    @Test
    void testNullifyChannel() {
        Path origImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), config.getProperty(Constants.ORIG_IMAGE_NAME));
        Mat image = imageService.readImage(origImagePath.toString());
        Mat processedImage = imageService.nullifyChannel(image, Channel.GREEN);
    }

    @Test
    void testWriteImage() {
        Path origImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), config.getProperty(Constants.ORIG_IMAGE_NAME));
        Mat image = imageService.readImage(origImagePath.toString());
        Mat processedImage = imageService.nullifyChannel(image, Channel.RED);

        String processedImagePath = config.getProperty(Constants.IMAGE_DIR_PATH) + config.getProperty(Constants.PROCESSED_IMAGE_NAME);
        imageService.writeImage(processedImage, processedImagePath);
    }
}
