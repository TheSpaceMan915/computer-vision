package app;

import app.services.ImageIOService;
import app.services.ImageSegmentationService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class ImageSegmentationServiceTest {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final Config config = new Config();

    private final ImageIOService imageIOService = new ImageIOService();

    private final ImageSegmentationService imageSegmentationService = new ImageSegmentationService();

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
    void testApplyFloodFill() {
        Point start = new Point(150, 150);
        Scalar colour = new Scalar(72, 218, 179);
//        Scalar colour = null;
        Scalar lowerDiff = new Scalar(0, 0, 0);
//        Scalar lowerDiff = null;
        Scalar upperDiff = new Scalar(10, 10, 10);
//        Scalar upperDiff = null;
        Optional<Mat> optFilled = imageSegmentationService.applyFloodFill(
                original1,
                start,
                colour,
                lowerDiff,
                upperDiff);

        boolean isSaved = false;
        if (optFilled.isPresent()) {
            Mat filled = optFilled.get();
            Path processedImage = Paths.get(imageDirPath, "processed", "filled_" + origImageName1);
            isSaved = imageIOService.writeImage(filled, processedImage.toString());
        }
        Assertions.assertTrue(isSaved);
    }
}
