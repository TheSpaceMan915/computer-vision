package app;

import app.services.ImageFilteringService;
import app.services.ImageIOService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.opencv.core.Mat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class ImageFilteringServiceTest {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final Config config = new Config();

    private final ImageIOService imageIOService = new ImageIOService();

    private final ImageFilteringService imageFilteringService = new ImageFilteringService();

    private Mat original1;

    private Mat original2;

    private Mat original3;

    @BeforeEach
    void readImages() {
        Path origImagePath1 = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "original", config.getProperty(Constants.FIRST_IMAGE_NAME));
        Path origImagePath2 = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "original", config.getProperty(Constants.SECOND_IMAGE_NAME));
        Path origImagePath3 = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "original", config.getProperty(Constants.THIRD_IMAGE_NAME));

        Optional<Mat> optOriginal1 = imageIOService.readImage(origImagePath1.toString());
        Optional<Mat> optOriginal2 = imageIOService.readImage(origImagePath2.toString());
        Optional<Mat> optOriginal3 = imageIOService.readImage(origImagePath3.toString());
        Assertions.assertTrue(optOriginal1.isPresent());
        Assertions.assertTrue(optOriginal2.isPresent());
        Assertions.assertTrue(optOriginal3.isPresent());

        original1 = optOriginal1.get();
        original2 = optOriginal2.get();
        original3 = optOriginal3.get();
    }

    @Test
    void testConvertToGrayscale() {
        Mat grayscale = imageFilteringService.convertToGrayscale(original1);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "grayscale_" + config.getProperty(Constants.FIRST_IMAGE_NAME));
        boolean isSaved = imageIOService.writeImage(grayscale, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testApplyGaussianBlur() {
        Mat blurred = imageFilteringService.applyGaussianBlur(original2, 3, 3);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "blurred_" + config.getProperty(Constants.SECOND_IMAGE_NAME));
        boolean isSaved = imageIOService.writeImage(blurred, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }
}
