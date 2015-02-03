package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.tracker;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;

/**
 * Filter that given an image outputs the position of the animal
 */

public class TrackingFilter implements Filter<ImageWithCorners, AnimalPosition> {

    @Override
    public AnimalPosition filter(ImageWithCorners input) {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
