package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;

import java.io.IOException;

/**
 * A filter that calls another filter and passes the input data through to the
 * output
 */
public class ImageWithCornersClonePassthroughFilter<S extends ImageWithCorners, D> implements
        Filter<S, PassthroughFilter.Passthrough<ImageWithCorners, D>> {
    private final Filter<S, D> filter;

    public ImageWithCornersClonePassthroughFilter(Filter<S, D> filter) {
        this.filter = filter;
    }

    @Override
    public PassthroughFilter.Passthrough<ImageWithCorners, D> filter(S input) {
        ImageWithCorners clone = new ImageWithCorners(input);
        return new PassthroughFilter.Passthrough<>(clone, filter.filter(input));
    }

    @Override
    public void close() throws IOException {
        filter.close();
    }
}
