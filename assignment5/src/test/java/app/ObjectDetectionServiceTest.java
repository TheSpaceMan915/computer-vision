package app;

import app.services.ImageIOService;
import app.services.ObjectDetectionService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class ObjectDetectionServiceTest {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final Config config = new Config();

    private final ImageIOService imageIOService = new ImageIOService();

    private final ObjectDetectionService objectDetectionService = new ObjectDetectionService();

    private final String imageDirPath = config.getProperty(Constants.IMAGE_DIR_PATH);

    private final String origImageName4 = config.getProperty(Constants.FOURTH_IMAGE_NAME);

    private Mat original4;


    @BeforeEach
    void readImages() {
        Path origImage4 = Paths.get(imageDirPath, "original", origImageName4);
        original4 = imageIOService.readImage(origImage4.toString()).orElseThrow();
    }

    @Test
    void testDetectRectangles() {
        Path debugDir = Paths.get(imageDirPath, "processed", "rectangle-detection");
        Optional<List<Mat>> optRectangles =
                objectDetectionService.detectRectangles(
                        original4,
                        new Size(70, 60),
                        debugDir.toString());
        Assertions.assertTrue(optRectangles.isPresent());
    }
}
