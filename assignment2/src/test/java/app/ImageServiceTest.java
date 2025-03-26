package app;

import org.junit.jupiter.api.Test;

import org.opencv.core.Mat;

public class ImageServiceTest {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final Config config = new Config();

    private final ImageService imageService = new ImageService();

    @Test
    void testImageLoading() {
        String origImagePath = config.getProperty(Constants.IMAGE_DIR_PATH) + config.getProperty(Constants.ORIG_IMAGE_NAME);
        Mat mat = imageService.loadImage(origImagePath);
    }
}
