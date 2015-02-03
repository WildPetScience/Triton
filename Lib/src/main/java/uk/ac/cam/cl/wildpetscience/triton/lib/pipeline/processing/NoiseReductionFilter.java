package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.processing;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;

/**
 * Reduce noise in the image captured by the camera.
 */
public class NoiseReductionFilter implements Filter<Image, Image> {
    @Override
    public Image filter(Image input) {
        // TODO: Provide implementation
        return input;
    }

    @Override
    public void close() throws IOException {

    }
}
