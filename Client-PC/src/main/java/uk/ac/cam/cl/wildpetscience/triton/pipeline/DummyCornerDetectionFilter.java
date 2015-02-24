package uk.ac.cam.cl.wildpetscience.triton.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;

/**
 * Adds fake corner detection output to a picture.
 */
public class DummyCornerDetectionFilter implements Filter<Image, ImageWithCorners> {
    @Override
    public ImageWithCorners filter(Image input) {
        // Move semantics - don't call clone(). The var input is no longer valid.
        return new ImageWithCorners(input.getData());
    }

    @Override
    public void close() throws IOException {

    }
}
