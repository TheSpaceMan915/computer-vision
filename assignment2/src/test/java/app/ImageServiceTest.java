package app;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;

import org.opencv.core.Mat;

import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class ImageServiceTest {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final Config config = new Config();

    private final ImageService imageService = new ImageService();

    @Test
    void testImageLoading() {
        Path origImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), config.getProperty(Constants.ORIG_IMAGE_NAME));
        Mat image = imageService.loadImage(origImagePath.toString());
    }

    @Test
    void testNullifyChannel() {
        Path origImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), config.getProperty(Constants.ORIG_IMAGE_NAME));
        Mat image = imageService.loadImage(origImagePath.toString());
        Mat processedImage = imageService.nullifyChannel(image, 1);
    }
}
