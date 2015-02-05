package uk.ac.cam.cl.wildpetscience.triton.pi.camera;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.InputFailedException;

import java.io.IOException;

/**
 * An ImageInputSource that fetches images from a Raspberry Pi camera.
 */
public class PiCameraInputSource implements ImageInputSource {
    @Override
    public Image getNext() throws InputFailedException {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
