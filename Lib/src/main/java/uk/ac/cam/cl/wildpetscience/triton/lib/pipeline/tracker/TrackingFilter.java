package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.tracker;

import org.opencv.core.Point;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Filter that given an image outputs the position of the animal
 */

public class TrackingFilter implements Filter<ImageWithCorners, AnimalPosition> {

    @Override
    public AnimalPosition filter(ImageWithCorners input) {
        return new AnimalPosition(new Point(0.5, 0.5), LocalDateTime.now(), 0.4);
    }

    @Override
    public void close() throws IOException {

    }
}
