package uk.ac.cam.cl.wildpetscience.triton.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;

/**
 * Transforms an image within its corners
 */
public class CornerTransformFilter implements Filter<ImageWithCorners, Image> {
    @Override
    public Image filter(ImageWithCorners input) {
        Image result = input.getInteriorTransform();
        input.release();
        return result;
    }

    @Override
    public void close() throws IOException {

    }
}
