package app;

import app.services.ImageConcatenationService;
import app.services.ImageIOService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.opencv.core.Mat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ImageConcatenationServiceTest {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final Config config = new Config();

    private final ImageIOService imageIOService = new ImageIOService();

    private final ImageConcatenationService imageConcatenationService = new ImageConcatenationService();

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
    void testHconcat() {
        List<Mat> images = Arrays.asList(original2, original3);
        Optional<Mat> optConcatenated = imageConcatenationService.hconcat(images);
        boolean isSaved = false;
        if (optConcatenated.isPresent()) {
            Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "hconcatenated.jpg");
            isSaved = imageIOService.writeImage(optConcatenated.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testVconcat() {
        List<Mat> images = Arrays.asList(original1, original2, original3);
        Optional<Mat> optConcatenated = imageConcatenationService.vconcat(images);
        boolean isSaved = false;
        if (optConcatenated.isPresent()) {
            Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "vconcatenated.jpg");
            isSaved = imageIOService.writeImage(optConcatenated.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }
}
