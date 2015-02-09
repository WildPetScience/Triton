package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

/**
 * Implements an output sink that just ignores all the data fed to it.
 * @param <T>
 */
public class IgnoreOutputSink<T> implements OutputSink<T> {

    public void onDataAvailable(T data) {}
    public void close() {}

}
