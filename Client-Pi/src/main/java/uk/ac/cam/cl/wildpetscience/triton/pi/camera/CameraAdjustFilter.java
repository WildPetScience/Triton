package uk.ac.cam.cl.wildpetscience.triton.pi.camera;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;

/**
 * Passes images through and checks them for brightness levels and adjusts the
 * camera accordingly.
 */
public class CameraAdjustFilter implements Filter<Image, Image> {
    private final Camera camera;

    public CameraAdjustFilter(Camera camera) {
        this.camera = camera;
    }

    @Override
    public Image filter(Image input) {
        // TODO: Look at camera output and decide on how do adjust it.
        return input;
    }

    @Override
    public void close() throws IOException {

    }
}
