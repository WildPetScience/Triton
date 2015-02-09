package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;

/**
 * Implements an output sink that just ignores all the data fed to it.
 * @param <T>
 */
public class IgnoreOutputSink<T> implements OutputSink<T> {

    public void onDataAvailable(T data) {
        if (data instanceof Image) {
            ((Image)data).release();
        }
    }
    public void close() {}

}
