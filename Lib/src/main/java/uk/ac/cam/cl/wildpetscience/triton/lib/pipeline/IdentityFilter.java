package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import java.io.IOException;

/**
 * The id function
 */
public class IdentityFilter<T> implements Filter<T, T> {
    @Override
    public T filter(T input) {
        return input;
    }

    @Override
    public void close() throws IOException {
    }
}
