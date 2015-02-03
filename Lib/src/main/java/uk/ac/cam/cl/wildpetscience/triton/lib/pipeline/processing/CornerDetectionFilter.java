package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.processing;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;

/**
 * Detects corners in an image based on a sample pattern
 */
public class CornerDetectionFilter implements Filter<Image, ImageWithCorners> {
    @Override
    public ImageWithCorners filter(Image input) {
        // TODO: Provide implementation
        ImageWithCorners imageWithCorners = new ImageWithCorners(input);
        input.release();
        return imageWithCorners;
    }

    @Override
    public void close() throws IOException {

    }
}
