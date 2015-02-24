package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import java.io.IOException;

/**
 * Makes a type more generic
 */
public class ReducingFilter<S extends D, D> implements Filter<S, D> {
    @Override
    public D filter(S input) {
        return input;
    }

    @Override
    public void close() throws IOException {
    }
}
