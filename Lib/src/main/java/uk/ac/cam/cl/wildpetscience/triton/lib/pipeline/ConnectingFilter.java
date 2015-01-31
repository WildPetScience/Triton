package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import java.io.IOException;

/**
 * A filter that combines two filters. Takes ownership of the passed filters
 * (in that it will call their close methods)
 */
public class ConnectingFilter<S, I, D> implements Filter<S, D> {

    private final Filter<S, I> f1;
    private final Filter<I, D> f2;

    public ConnectingFilter(Filter<S, I> f1, Filter<I, D> f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    public D filter(S input) {
        return f2.filter(f1.filter(input));
    }

    @Override
    public void close() throws IOException {
        f1.close();
        f2.close();
    }
}
