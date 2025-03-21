package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OpenCVLoaderTest {

    @Test
    public void testOpenCVInitialisation() {
        System.out.println("OpenCV initialisation test started");
        assertDoesNotThrow(OpenCVLoader::new);
        System.out.println("OpenCV initialisation test finished");
    }
}
