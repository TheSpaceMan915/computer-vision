package app;

import lombok.extern.log4j.Log4j2;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
* Provides functionality to process images.
*/
@Log4j2
public class ImageService {

    /*
    * Load an image from an absolute path.
    * */
    public Mat loadImage(String imagePath) {
        Path path = Paths.get(imagePath);
        log.info("Loading image from '{}'", path);
        
        if (!Files.exists(path)) {
            log.warn("Could not find image at '{}'", path);
            return null;
        }
        log.info("The image was successfully loaded");
        return Imgcodecs.imread(path.toString());
    }
}
