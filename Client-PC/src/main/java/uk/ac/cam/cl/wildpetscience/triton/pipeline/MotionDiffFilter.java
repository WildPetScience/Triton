package uk.ac.cam.cl.wildpetscience.triton.pipeline;

import org.opencv.core.Mat;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.tracker.TrackingFilter;

import java.io.IOException;

/**
 * A filter that extracts the motion difference from the TrackingFilter
 */
public class MotionDiffFilter implements Filter<ImageWithCorners, Image> {

    private TrackingFilter trackingFilter;

    private int count = 0;

    public MotionDiffFilter(TrackingFilter trackingFilter) {
        this.trackingFilter = trackingFilter;
    }

    @Override
    public Image filter(ImageWithCorners input) {
        trackingFilter.filter(input);
        if (count++ < 3) {
            return new Image();
        }
        return new Image(trackingFilter.getDiff());
    }

    @Override
    public void close() throws IOException {
        trackingFilter.close();
    }
}
