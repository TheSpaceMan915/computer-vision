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

    private final String origImageName4 = config.getProperty(Constants.FOURTH_IMAGE_NAME);

    private Mat original1;

    private Mat original2;

    private Mat original3;

    private Mat original4;

    @BeforeEach
    void readImages() {
        Path origImage1 = Paths.get(imageDirPath, "original", origImageName1);
        Path origImage2 = Paths.get(imageDirPath, "original", origImageName2);
        Path origImage3 = Paths.get(imageDirPath, "original", origImageName3);
        Path origImage4 = Paths.get(imageDirPath, "original", origImageName4);

        original1 = imageIOService.readImage(origImage1.toString()).orElseThrow();
        original2 = imageIOService.readImage(origImage2.toString()).orElseThrow();
        original3 = imageIOService.readImage(origImage3.toString()).orElseThrow();
        original4 = imageIOService.readImage(origImage4.toString()).orElseThrow();
    }

    @Test
    void testApplyNormalizedFilter() {
        Optional<Mat> optBlurred = imageFilteringService.applyFilter(original1, 3, FilterType.NORMALIZED);
        if (optBlurred.isPresent()) {
            Mat blurred = optBlurred.get();
            Path processedImage = Paths.get(imageDirPath, "processed", "normalized_" + origImageName1);
            boolean isSaved = imageIOService.writeImage(blurred, processedImage.toString());
            Assertions.assertTrue(isSaved);

            HighGui.imshow("Normalized Filter", blurred);
            HighGui.waitKey();
        }
    }

    @Test
    void testApplyGaussianFilter() {
        Optional<Mat> optBlurred = imageFilteringService.applyFilter(original2, 5, FilterType.GAUSSIAN);
        if (optBlurred.isPresent()) {
            Mat blurred = optBlurred.get();
            Path processedImage = Paths.get(imageDirPath, "processed", "Gaussian_" + origImageName2);
            boolean isSaved = imageIOService.writeImage(blurred, processedImage.toString());
            Assertions.assertTrue(isSaved);

            HighGui.imshow("Gaussian Filter", blurred);
            HighGui.waitKey();
        }
    }

    @Test
    void testApplyMedianFilter() {
        Optional<Mat> optBlurred = imageFilteringService.applyFilter(original3, 7, FilterType.MEDIAN);
        if (optBlurred.isPresent()) {
            Mat blurred = optBlurred.get();
            Path processedImage = Paths.get(imageDirPath, "processed", "median_" + origImageName3);
            boolean isSaved = imageIOService.writeImage(blurred, processedImage.toString());
            Assertions.assertTrue(isSaved);

            HighGui.imshow("Median Filter", blurred);
            HighGui.waitKey();
        }
    }

    @Test
    void testApplyBilateralFilter() {
        Optional<Mat> optBlurred = imageFilteringService.applyFilter(original1, 3, FilterType.BILATERAL);
        if (optBlurred.isPresent()) {
            Mat blurred = optBlurred.get();
            Path processedImage = Paths.get(imageDirPath, "processed", "bilateral_" + origImageName1);
            boolean isSaved = imageIOService.writeImage(blurred, processedImage.toString());
            Assertions.assertTrue(isSaved);

            HighGui.imshow("Bilateral Filter", blurred);
            HighGui.waitKey();
        }
    }

    @Test
    void testApplyErosionWithCross() {
        for (int kernelSize = 3; kernelSize < 16; kernelSize += 2) {
            MorphShape morphShape = MorphShape.CROSS;
            Optional<Mat> optEroded = imageFilteringService.applyErosion(
                    original4,
                    kernelSize,
                    morphShape);
            boolean isSaved = false;
            if (optEroded.isPresent()) {
                Mat eroded = optEroded.get();
                Path processedImage = Paths.get(
                        imageDirPath,
                        "processed",
                        "cross-erosion",
                        "eroded" + "_cross" + "_kernel" + kernelSize + "_" + origImageName4);
                isSaved = imageIOService.writeImage(eroded, processedImage.toString());

                HighGui.imshow("Erosion with Kernel"  + kernelSize, eroded);
                HighGui.waitKey();
            }
            Assertions.assertTrue(isSaved);
        }
    }

    @Test
    void testApplyErosionWithRectangle() {
        for (int kernelSize = 3; kernelSize < 16; kernelSize += 2) {
            MorphShape morphShape = MorphShape.RECTANGLE;
            Optional<Mat> optEroded = imageFilteringService.applyErosion(
                    original4,
                    kernelSize,
                    morphShape);
            boolean isSaved = false;
            if (optEroded.isPresent()) {
                Mat eroded = optEroded.get();
                Path processedImage = Paths.get(
                        imageDirPath,
                        "processed",
                        "rectangle-erosion",
                        "eroded" + "_rectangle" + "_kernel" + kernelSize + "_" + origImageName4);
                isSaved = imageIOService.writeImage(eroded, processedImage.toString());

                HighGui.imshow("Erosion with Kernel"  + kernelSize, eroded);
                HighGui.waitKey();
            }
            Assertions.assertTrue(isSaved);
        }
    }

    @Test
    void testApplyDilationWithEllipse() {
        for (int kernelSize = 3; kernelSize < 16; kernelSize += 2) {
            MorphShape morphShape = MorphShape.ELLIPSE;
            Optional<Mat> optDilated = imageFilteringService.applyDilation(
                    original4,
                    kernelSize,
                    morphShape);
            boolean isSaved = false;
            if (optDilated.isPresent()) {
                Mat dilated = optDilated.get();
                Path processedImage = Paths.get(
                        imageDirPath,
                        "processed",
                        "ellipse-dilation",
                        "dilated" + "_ellipse" + "_kernel" + kernelSize + "_" + origImageName4);
                isSaved = imageIOService.writeImage(dilated, processedImage.toString());

                HighGui.imshow("Dilation with Kernel"  + kernelSize, dilated);
                HighGui.waitKey();
            }
            Assertions.assertTrue(isSaved);
        }
    }
}
