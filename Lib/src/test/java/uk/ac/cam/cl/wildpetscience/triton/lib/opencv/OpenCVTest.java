package uk.ac.cam.cl.wildpetscience.triton.lib.opencv;

import nu.pattern.OpenCV;
import org.junit.Test;

/**
 * Tests that OpenCV and JavaCV are working.
 */
public class OpenCVTest {
    @Test
    public void testLoadOpenCV() {
        // Load a class to test OpenCV library loading
        OpenCV.loadShared();
    }
}
