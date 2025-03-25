package app;

import org.junit.jupiter.api.Test;

import org.opencv.core.Mat;

public class ImageServiceTest {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final AppConfig appConfig = new AppConfig();

    private final ImageService imageService = new ImageService();

    @Test
    void testImageLoading() {
        String origImagePath = appConfig.getProperty(Constants.IMAGE_DIR_PATH) + appConfig.getProperty(Constants.ORIG_IMAGE_NAME);
        Mat mat = imageService.loadImage(origImagePath);
    }
}
