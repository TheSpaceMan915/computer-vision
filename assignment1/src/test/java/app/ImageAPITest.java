package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ImageAPITest {

    @Test
    public void testOpenCVInitialisation() {
        System.out.println("OpenCV initialisation test started");
        assertDoesNotThrow(ImageAPI::new);
        System.out.println("OpenCV initialisation test finished");
    }
}
