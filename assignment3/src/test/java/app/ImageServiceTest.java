package app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ImageServiceTest {

    private final OpenCVLoader loader = new OpenCVLoader();

    private final Config config = new Config();

    private final ImageService imageService = new ImageService();

    private Mat original1;

    private Mat original2;

    private Mat original3;

    @BeforeEach
    void readImages() {
        Path origImagePath1 = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "original", config.getProperty(Constants.FIRST_IMAGE_NAME));
        Path origImagePath2 = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "original", config.getProperty(Constants.SECOND_IMAGE_NAME));
        Path origImagePath3 = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "original", config.getProperty(Constants.THIRD_IMAGE_NAME));

        Optional<Mat> optOriginal1 = imageService.readImage(origImagePath1.toString());
        Optional<Mat> optOriginal2 = imageService.readImage(origImagePath2.toString());
        Optional<Mat> optOriginal3 = imageService.readImage(origImagePath3.toString());
        Assertions.assertTrue(optOriginal1.isPresent());
        Assertions.assertTrue(optOriginal2.isPresent());
        Assertions.assertTrue(optOriginal3.isPresent());

        original1 = optOriginal1.get();
        original2 = optOriginal2.get();
        original3 = optOriginal3.get();
    }

    @Test
    void testConvertToGrayscale() {
        Mat grayscale = imageService.convertToGrayscale(original1);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "grayscale_" + config.getProperty(Constants.FIRST_IMAGE_NAME));
        boolean isSaved = imageService.writeImage(grayscale, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testApplyGaussianBlur() {
        Mat blurred = imageService.applyGaussianBlur(original2, 3, 3);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "blurred_" + config.getProperty(Constants.SECOND_IMAGE_NAME));
        boolean isSaved = imageService.writeImage(blurred, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testApplySobelOperator() {
        Mat blurred = imageService.applyGaussianBlur(original3, 3, 3);
        Mat grayscale = imageService.convertToGrayscale(blurred);
        Mat edges = imageService.applySobelOperator(grayscale, CvType.CV_16S, 3);
//        Mat edges = imageService.applySobelOperator(grayscale, CvType.CV_16S, 1);
//        Mat edges = imageService.applySobelOperator(grayscale, CvType.CV_8U, 3);

        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "Sobel_" + config.getProperty(Constants.THIRD_IMAGE_NAME));
        boolean isSaved = imageService.writeImage(edges, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testApplyLaplacianOperator() {
        Mat blurred = imageService.applyGaussianBlur(original1, 3, 3);
        Mat grayscale = imageService.convertToGrayscale(blurred);
        Mat edgeImage = imageService.applyLaplacianOperator(grayscale, CvType.CV_16S, 3);
//        Mat edgeImage = imageService.applyLaplacianOperator(grayscale, CvType.CV_16S, 1);
//        Mat edgeImage = imageService.applyLaplacianOperator(grayscale, CvType.CV_8U, 3);

        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "Laplacian_" + config.getProperty(Constants.FIRST_IMAGE_NAME));
        boolean isSaved = imageService.writeImage(edgeImage, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testFlip() {
        Mat flipped = imageService.flip(original2, Axis.X);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "flipped_" + config.getProperty(Constants.SECOND_IMAGE_NAME));
        boolean isSaved = imageService.writeImage(flipped, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testRepeat() {
        Mat repeated = imageService.repeat(original3, 2, 2);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "repeated_" + config.getProperty(Constants.THIRD_IMAGE_NAME));
        boolean isSaved = imageService.writeImage(repeated, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testHconcat() {
        List<Mat> images = Arrays.asList(original2, original3);
        Optional<Mat> optConcatenated = imageService.hconcat(images);
        boolean isSaved = false;
        if (optConcatenated.isPresent()) {
            Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "hconcatenated.jpg");
            isSaved = imageService.writeImage(optConcatenated.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testVconcat() {
        List<Mat> images = Arrays.asList(original1, original2, original3);
        Optional<Mat> optConcatenated = imageService.vconcat(images);
        boolean isSaved = false;
        if (optConcatenated.isPresent()) {
            Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "vconcatenated.jpg");
            isSaved = imageService.writeImage(optConcatenated.get(), processedImagePath.toString());
        }
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testResize() {
        Mat resized = imageService.resize(original1, 200, 100);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "resized_" + config.getProperty(Constants.FIRST_IMAGE_NAME));
        boolean isSaved = imageService.writeImage(resized, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testRotateWithCropFalse() {
        Mat rotated = imageService.rotate(original2, 90, false);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "rotated_with_crop_false_" + config.getProperty(Constants.SECOND_IMAGE_NAME));
        boolean isSaved = imageService.writeImage(rotated, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }

    @Test
    void testRotateWithCropTrue() {
        Mat rotated = imageService.rotate(original2, 90, true);
        Path processedImagePath = Paths.get(config.getProperty(Constants.IMAGE_DIR_PATH), "processed", "rotated_with_crop_true_" + config.getProperty(Constants.SECOND_IMAGE_NAME));
        boolean isSaved = imageService.writeImage(rotated, processedImagePath.toString());
        Assertions.assertTrue(isSaved);
    }
}
