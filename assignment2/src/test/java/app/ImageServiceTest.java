package app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.opencv.core.Mat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class ImageServiceTest {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final Config config = new Config();

    private final ImageService imageService = new ImageService();

    private final String imageDirPath = config.getProperty(Constants.IMAGE_DIR_PATH);

    private final Path origImage = Paths.get(imageDirPath, config.getProperty(Constants.ORIG_IMAGE_NAME));

    @Test
    void testReadImage() {
        Mat image = imageService.readImage(origImage.toString());
    }

    @Test
    void testNullifyChannel() {
        Mat image = imageService.readImage(origImage.toString());
        Mat processedImage = imageService.nullifyChannel(image, Channel.GREEN).orElseThrow();
    }

    @Test
    void testWriteImage() {
        Mat image = imageService.readImage(origImage.toString());
        Optional<Mat> optProcessed = imageService.nullifyChannel(image, Channel.RED);
        boolean isSaved = false;
        if (optProcessed.isPresent()) {
            String processedImagePath = imageDirPath + config.getProperty(Constants.PROCESSED_IMAGE_NAME);
            isSaved = imageService.writeImage(optProcessed.get(), processedImagePath);
        }
        Assertions.assertTrue(isSaved);
    }
}
