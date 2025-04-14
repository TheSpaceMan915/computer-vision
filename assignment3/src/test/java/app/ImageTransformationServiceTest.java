package app;

import app.services.ImageIOService;
import app.services.ImageTransformationService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.opencv.core.Mat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class ImageTransformationServiceTest {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final Config config = new Config();

    private final ImageIOService imageIOService = new ImageIOService();

    private final ImageTransformationService imageTransformationService = new ImageTransformationService();

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
    void testFlip() {
        Mat flipped = imageTransformationService.flip(original2, Axis.X);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "flipped_" + config.getProperty(Constants.SECOND_IMAGE_NAME));
        boolean isSaved = imageIOService.writeImage(flipped, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testRepeat() {
        Mat repeated = imageTransformationService.repeat(original3, 2, 2);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "repeated_" + config.getProperty(Constants.THIRD_IMAGE_NAME));
        boolean isSaved = imageIOService.writeImage(repeated, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testResize() {
        Mat resized = imageTransformationService.resize(original1, 200, 100);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "resized_" + config.getProperty(Constants.FIRST_IMAGE_NAME));
        boolean isSaved = imageIOService.writeImage(resized, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testRotateWithCropFalse() {
        Mat rotated = imageTransformationService.rotate(original2, 90, false);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "rotated_with_crop_false_" + config.getProperty(Constants.SECOND_IMAGE_NAME));
        boolean isSaved = imageIOService.writeImage(rotated, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testRotateWithCropTrue() {
        Mat rotated = imageTransformationService.rotate(original2, 90, true);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "rotated_with_crop_true_" + config.getProperty(Constants.SECOND_IMAGE_NAME));
        boolean isSaved = imageIOService.writeImage(rotated, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testTranslate() {
        Mat translated = imageTransformationService.translate(original3, 20, 20);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "translated_" + config.getProperty(Constants.THIRD_IMAGE_NAME));
        boolean isSaved = imageIOService.writeImage(translated, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testTransformPerspective() {
        Optional<Mat> optTransformed = imageTransformationService.transformPerspective(original1, 30, 20);
        boolean isSaved = false;
        if (optTransformed.isPresent()) {
            Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "transformed_perspective_" + config.getProperty(Constants.FIRST_IMAGE_NAME));
            isSaved = imageIOService.writeImage(optTransformed.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }
}
