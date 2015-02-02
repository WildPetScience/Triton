package uk.ac.cam.cl.wildpetscience.triton.lib.opencv;

import nu.pattern.OpenCV;
import org.junit.Test;
import uk.ac.cam.cl.wildpetscience.triton.lib.Bootstrap;

/**
 * Tests that OpenCV and JavaCV are working.
 */
public class OpenCVTest {
    @Test
    public void testLoadOpenCV() {
        Bootstrap.init();
    }
}
