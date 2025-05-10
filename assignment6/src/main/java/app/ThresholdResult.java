package app;

import org.opencv.core.Mat;

public record ThresholdResult(
        double threshold,
        Mat thresholded) {
}
