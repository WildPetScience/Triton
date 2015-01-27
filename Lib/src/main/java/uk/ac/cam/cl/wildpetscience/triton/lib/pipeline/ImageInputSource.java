package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;

import java.io.Closeable;

/**
 * A source of images.
 */
public interface ImageInputSource extends Closeable {

    /**
     * Gets the next image from the source.
     * @return The next image, or <code>null</code> if there are no more images.
     * @throws InputFailedException
     */
    public Image getNext() throws InputFailedException;
}
