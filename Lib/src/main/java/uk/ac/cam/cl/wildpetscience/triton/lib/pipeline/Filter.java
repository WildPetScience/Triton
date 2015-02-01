package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import java.io.Closeable;

/**
 * A filter class. Receives input, does something and returns output.
 * This class uses generics so that intermediate filters can contain more data
 * than just an Image.
 */
public interface Filter<S, D> extends Closeable {
    /**
     * Filters the input of type S to produce output of type D.
     * @param input a non-null input.
     * @return an output that must be non-null
     */
    public D filter(S input);
}
