package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import java.io.IOException;

/**
 * A filter that calls another filter and passes the input data through to the
 * output
 */
public class PassthroughFilter<S, D> implements Filter<S, PassthroughFilter.Passthrough<S, D>> {
    private final Filter<S, D> filter;

    public PassthroughFilter(Filter<S, D> filter) {
        this.filter = filter;
    }

    @Override
    public Passthrough<S, D> filter(S input) {
        return new Passthrough<>(input, filter.filter(input));
    }

    @Override
    public void close() throws IOException {
        filter.close();
    }

    public static class Passthrough<S, D> {
        public final S passthrough;
        public final D data;

        public Passthrough(S passthrough, D data) {
            this.passthrough = passthrough;
            this.data = data;
        }
    }
}
