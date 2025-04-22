package app;

import app.services.ImageFilteringService;
import app.services.ImageIOService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import java.nio.file.Path;
import java.nio.file.Paths;

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
    void testApplyNormalizedFilter() {
        Mat blurred = imageFilteringService.applyFilter(original1, 3, FilterType.NORMALIZED);
        Path processedImage = Paths.get(imageDirPath, "processed", "normalized_" + origImageName1);
        boolean isSaved = imageIOService.writeImage(blurred, processedImage.toString());
        Assertions.assertTrue(isSaved);

        HighGui.imshow("Normalized Filter", blurred);
        HighGui.waitKey();
    }

    @Test
    void testApplyGaussianFilter() {
        Mat blurred = imageFilteringService.applyFilter(original2, 5, FilterType.GAUSSIAN);
        Path processedImage = Paths.get(imageDirPath, "processed", "Gaussian_" + origImageName2);
        boolean isSaved = imageIOService.writeImage(blurred, processedImage.toString());
        Assertions.assertTrue(isSaved);

        HighGui.imshow("Gaussian Filter", blurred);
        HighGui.waitKey();
    }

    @Test
    void testApplyMedianFilter() {
        Mat blurred = imageFilteringService.applyFilter(original3, 7, FilterType.MEDIAN);
        Path processedImage = Paths.get(imageDirPath, "processed", "median_" + origImageName3);
        boolean isSaved = imageIOService.writeImage(blurred, processedImage.toString());
        Assertions.assertTrue(isSaved);

        HighGui.imshow("Median Filter", blurred);
        HighGui.waitKey();
    }

    @Test
    void testApplyBilateralFilter() {
        Mat blurred = imageFilteringService.applyFilter(original1, 3, FilterType.BILATERAL);
        Path processedImage = Paths.get(imageDirPath, "processed", "bilateral_" + origImageName1);
        boolean isSaved = imageIOService.writeImage(blurred, processedImage.toString());
        Assertions.assertTrue(isSaved);

        HighGui.imshow("Bilateral Filter", blurred);
        HighGui.waitKey();
    }

}
