package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;

public class DummyTapFilter extends TapFilter {
    private DummyInputSource input = new DummyInputSource();

    @Override
    public ImageWithCorners getMostRecentInput() {
        try {
            Image in = input.getNext();
            ImageWithCorners result = new ImageWithCorners(input.getNext());
            in.release();
            return result;
        } catch (InputFailedException e) {
            return null;
        }
    }
}
