package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;

import java.io.Closeable;

/**
 * A destination for images
 */
public interface ImageOutputSink extends Closeable {
    /**
     * Another image is available for processing. This method will most likely
     * be called on a worker thread, but will be called on the same thread each
     * time. Note that it is the job of this method to release the Image once
     * done.
     * @param image
     */
    public void onImageAvailable(Image image);
}
