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

public class ImageEdgeDetectionServiceTest {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final Config config = new Config();

    private final ImageIOService imageIOService = new ImageIOService();

    private final ImageFilteringService imageFilteringService = new ImageFilteringService();

    private final ImageEdgeDetectionService imageEdgeDetectionService = new ImageEdgeDetectionService();

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
    void testApplySobelOperator() {
        Mat blurred = imageFilteringService
                .applyGaussianBlur(original3, 3, 3)
                .orElseThrow();
        Mat grayscale = imageFilteringService
                .convertToGrayscale(blurred)
                .orElseThrow();
        Mat edges = imageEdgeDetectionService
                .applySobelOperator(grayscale, CvType.CV_16S, 3)
                .orElseThrow();
//        Mat edges = imageEdgeDetectionService
//                .applySobelOperator(grayscale, CvType.CV_16S, 1)
//                .orElseThrow();
//        Mat edges = imageEdgeDetectionService
//                .applySobelOperator(grayscale, CvType.CV_8U, 3)
//                .orElseThrow();

        Path processedImagePath = Paths.get(imageDirPath, "processed", "Sobel_" + origImageName3);
        boolean isSaved = imageIOService.writeImage(edges, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testApplyLaplacianOperator() {
        Mat blurred = imageFilteringService
                .applyGaussianBlur(original1, 3, 3)
                .orElseThrow();
        Mat grayscale = imageFilteringService
                .convertToGrayscale(blurred)
                .orElseThrow();
        Mat edgeImage = imageEdgeDetectionService
                .applyLaplacianOperator(grayscale, CvType.CV_16S, 3)
                .orElseThrow();
//        Mat edgeImage = imageEdgeDetectionService
//                .applyLaplacianOperator(grayscale, CvType.CV_16S, 1)
//                .orElseThrow();
//        Mat edgeImage = imageEdgeDetectionService
//                .applyLaplacianOperator(grayscale, CvType.CV_8U, 3)
//                .orElseThrow();

        Path processedImagePath = Paths.get(imageDirPath, "processed", "Laplacian_" + origImageName1);
        boolean isSaved = imageIOService.writeImage(edgeImage, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testApplyCanny() {
        final double threshold = 0.25;
        Mat edges = imageEdgeDetectionService
                .applyCanny(original2, threshold, threshold * 2)
                .orElseThrow();
        Path processedImagePath = Paths.get(imageDirPath, "processed", "Canny_" + origImageName2);
        boolean isSaved = imageIOService.writeImage(edges, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }
}
