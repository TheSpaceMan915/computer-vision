package app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageServiceTest {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final Config config = new Config();

    private final ImageService imageService = new ImageService();

    @Test
    void testBGRToGrayscale() {
        Path origImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "original", config.getProperty(Constants.FIRST_IMAGE_NAME));
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "grayscale_" + config.getProperty(Constants.FIRST_IMAGE_NAME));
        Mat image = imageService.readImage(origImagePath.toString());

        Mat grayscaleImage = imageService.BGRToGrayscale(image);
        boolean isSaved = imageService.writeImage(grayscaleImage, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testApplyGaussianBlur() {
        Path origImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "original", config.getProperty(Constants.SECOND_IMAGE_NAME));
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "blurred_" + config.getProperty(Constants.SECOND_IMAGE_NAME));
        Mat image = imageService.readImage(origImagePath.toString());

        Mat blurredImage = imageService.applyGaussianBlur(image, new Size(3, 3));
        boolean isSaved = imageService.writeImage(blurredImage, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testApplySobelOperator() {
        Path origImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "original", config.getProperty(Constants.THIRD_IMAGE_NAME));
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "Sobel_" + config.getProperty(Constants.THIRD_IMAGE_NAME));
        Mat image = imageService.readImage(origImagePath.toString());

        Mat blurredImage = imageService.applyGaussianBlur(image, new Size(3, 3));
        Mat grayscaleImage = imageService.BGRToGrayscale(blurredImage);
        Mat edgeImage = imageService.applySobelOperator(grayscaleImage, CvType.CV_16S, 3);
//        Mat edgeImage = imageService.applySobelOperator(grayscaleImage, CvType.CV_16S, 1);
//        Mat edgeImage = imageService.applySobelOperator(grayscaleImage, CvType.CV_8U, 3);
        boolean isSaved = imageService.writeImage(edgeImage, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }
}
