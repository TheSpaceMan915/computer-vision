package app;

import app.services.ImageEdgeDetectionService;
import app.services.ImageFilteringService;
import app.services.ImageIOService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class ImageEdgeDetectionServiceTest {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final Config config = new Config();

    private final ImageIOService imageIOService = new ImageIOService();

    private final ImageFilteringService imageFilteringService = new ImageFilteringService();

    private final ImageEdgeDetectionService imageEdgeDetectionService = new ImageEdgeDetectionService();

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
    void testApplySobelOperator() {
        Mat blurred = imageFilteringService.applyGaussianBlur(original3, 3, 3);
        Mat grayscale = imageFilteringService.convertToGrayscale(blurred);
        Mat edges = imageEdgeDetectionService.applySobelOperator(grayscale, CvType.CV_16S, 3);
//        Mat edges = imageEdgeDetectionService.applySobelOperator(grayscale, CvType.CV_16S, 1);
//        Mat edges = imageEdgeDetectionService.applySobelOperator(grayscale, CvType.CV_8U, 3);

        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "Sobel_" + config.getProperty(Constants.THIRD_IMAGE_NAME));
        boolean isSaved = imageIOService.writeImage(edges, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testApplyLaplacianOperator() {
        Mat blurred = imageFilteringService.applyGaussianBlur(original1, 3, 3);
        Mat grayscale = imageFilteringService.convertToGrayscale(blurred);
        Mat edgeImage = imageEdgeDetectionService.applyLaplacianOperator(grayscale, CvType.CV_16S, 3);
//        Mat edgeImage = imageEdgeDetectionService.applyLaplacianOperator(grayscale, CvType.CV_16S, 1);
//        Mat edgeImage = imageEdgeDetectionService.applyLaplacianOperator(grayscale, CvType.CV_8U, 3);

        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "Laplacian_" + config.getProperty(Constants.FIRST_IMAGE_NAME));
        boolean isSaved = imageIOService.writeImage(edgeImage, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }
}
