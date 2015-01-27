package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;

import java.io.Closeable;

/**
 * A source of images.
 */
public interface ImageInputSource extends Closeable {

    public Image getNext() throws InputFailedException;
}
