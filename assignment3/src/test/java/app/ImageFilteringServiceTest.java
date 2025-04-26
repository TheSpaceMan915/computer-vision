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
    void testConvertToGrayscale() {
        Optional<Mat> optGrayscale = imageFilteringService.convertToGrayscale(original1);
        boolean isSaved = false;
        if (optGrayscale.isPresent()) {
            Path processedImagePath = Paths.get(imageDirPath, "processed", "grayscale_" + origImageName1);
            isSaved = imageIOService.writeImage(optGrayscale.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testApplyGaussianBlur() {
        Optional<Mat> optBlurred = imageFilteringService.applyGaussianBlur(original2, 3, 3);
        boolean isSaved = false;
        if (optBlurred.isPresent()) {
            Path processedImagePath = Paths.get(imageDirPath, "processed", "blurred_" + origImageName2);
            isSaved = imageIOService.writeImage(optBlurred.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }
}
