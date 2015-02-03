package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;

import java.io.Closeable;

/**
 * A destination for images
 */
public interface OutputSink<T> extends Closeable {
    /**
     * More data is available for processing. This method will most likely
     * be called on a worker thread, but will be called on the same thread each
     * time. Note that it is the job of this method to release any passed data
     * when done.
     * @param data
     */
    public void onDataAvailable(T data);
}
