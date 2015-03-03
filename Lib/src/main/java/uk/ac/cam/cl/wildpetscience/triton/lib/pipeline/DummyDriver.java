package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;

/**
 * A driver that does nothing but return constant images.
 */
public class DummyDriver extends Driver<Image> {
    public DummyDriver() {
        super(new DummyInputSource(), new IdentityFilter<>(), new IgnoreOutputSink<>());
        setTapFilter(new DummyTapFilter());
    }

    @Override
    public Image getMostRecentInput() {
        try {
            return in.getNext();
        } catch (InputFailedException e) {
            // DummyInputSource doesn't fail
            return null;
        }
    }

    @Override
    public void start() {
        // Nothing
    }
}
