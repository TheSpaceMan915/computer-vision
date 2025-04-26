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

    private final String imageDirPath = config.getProperty(Constants.IMAGE_DIR_PATH);

    private final String origImageName1 = config.getProperty(Constants.FIRST_IMAGE_NAME);

    private final String origImageName2 = config.getProperty(Constants.SECOND_IMAGE_NAME);

    private final String origImageName3 = config.getProperty(Constants.THIRD_IMAGE_NAME);

    private Mat original1;

    private Mat original2;

    private Mat original3;

    @BeforeEach
    void readImages() {
        Path origImage1 = Paths.get(imageDirPath, "original", origImageName1);
        Path origImage2 = Paths.get(imageDirPath, "original", origImageName2);
        Path origImage3 = Paths.get(imageDirPath, "original", origImageName3);

        original1 = imageIOService.readImage(origImage1.toString()).orElseThrow();
        original2 = imageIOService.readImage(origImage2.toString()).orElseThrow();
        original3 = imageIOService.readImage(origImage3.toString()).orElseThrow();
    }

    @Test
    void testFlip() {
        Optional<Mat> optFlipped = imageTransformationService.flip(original2, Axis.X);
        boolean isSaved = false;
        if (optFlipped.isPresent()) {
            Path processedImagePath = Paths.get(imageDirPath, "processed", "flipped_" + origImageName2);
            isSaved = imageIOService.writeImage(optFlipped.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testRepeat() {
        Optional<Mat> optRepeated = imageTransformationService.repeat(original3, 2, 2);
        boolean isSaved = false;
        if (optRepeated.isPresent()) {
            Path processedImagePath = Paths.get(imageDirPath, "processed", "repeated_" + origImageName3);
            isSaved = imageIOService.writeImage(optRepeated.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testResize() {
        Optional<Mat> optResized = imageTransformationService.resize(original1, 200, 100);
        boolean isSaved = false;
        if (optResized.isPresent()) {
            Path processedImagePath = Paths.get(imageDirPath, "processed", "resized_" + origImageName1);
            isSaved = imageIOService.writeImage(optResized.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testRotateWithoutCrop() {
        Optional<Mat> optRotated = imageTransformationService.rotate(original2, 90, false);
        boolean isSaved = false;
        if (optRotated.isPresent()) {
            Path processedImagePath = Paths.get(imageDirPath, "processed", "rotated_without_crop_" + origImageName2);
            isSaved = imageIOService.writeImage(optRotated.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testRotateWithCrop() {
        Optional<Mat> optRotated = imageTransformationService.rotate(original2, 90, true);
        boolean isSaved = false;
        if (optRotated.isPresent()) {
            Path processedImagePath = Paths.get(imageDirPath, "processed", "rotated_with_crop_" + origImageName2);
            isSaved = imageIOService.writeImage(optRotated.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testTranslate() {
        Optional<Mat> optTranslated = imageTransformationService.translate(original3, 20, 20);
        boolean isSaved = false;
        if (optTranslated.isPresent()) {
            Path processedImagePath = Paths.get(imageDirPath, "processed", "translated_" + origImageName3);
            isSaved = imageIOService.writeImage(optTranslated.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testTransformPerspective() {
        Optional<Mat> optTransformed = imageTransformationService.transformPerspective(original1, 30, 20);
        boolean isSaved = false;
        if (optTransformed.isPresent()) {
            Path processedImagePath = Paths.get(imageDirPath, "processed", "transformed_perspective_" + origImageName1);
            isSaved = imageIOService.writeImage(optTransformed.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }
}
