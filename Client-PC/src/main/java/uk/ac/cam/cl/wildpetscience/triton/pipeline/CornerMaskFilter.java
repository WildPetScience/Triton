package uk.ac.cam.cl.wildpetscience.triton.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.processing.CornerDetectionFilter;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.tracker.TrackingFilter;

import java.io.IOException;

/**
 * A filter that extracts the motion difference from the TrackingFilter
 */
public class CornerMaskFilter implements Filter<Image, Image> {

    public CornerMaskFilter() {
    }

    @Override
    public Image filter(Image input) {
        Image img = new Image(CornerDetectionFilter.createMask(input.getData()));
        input.release();
        return img;
    }

    @Override
    public void close() throws IOException {
    }
}
