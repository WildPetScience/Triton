package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.processing;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;

/**
 * Created by anto on 03.02.2015.
 */
public class NormalisationFilter implements Filter<Image, ImageWithCorners> {
    @Override
    public ImageWithCorners filter(Image input) {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
