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
    void testHconcat() {
        List<Mat> images = Arrays.asList(original2, original3);
        Optional<Mat> optConcatenated = imageConcatenationService.hconcat(images);
        boolean isSaved = false;
        if (optConcatenated.isPresent()) {
            Path processedImagePath = Paths.get(imageDirPath, "processed", "hconcatenated.jpg");
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
            Path processedImagePath = Paths.get(imageDirPath, "processed", "vconcatenated.jpg");
            isSaved = imageIOService.writeImage(optConcatenated.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }
}
