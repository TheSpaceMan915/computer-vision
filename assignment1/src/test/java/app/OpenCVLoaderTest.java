package app;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public class OpenCVLoaderTest {

    @Test
    void testOpenCVInitialisation() {
        log.info("OpenCV initialisation test started");
        assertDoesNotThrow(OpenCVLoader::new);
        log.info("OpenCV initialisation test finished");
    }
}
