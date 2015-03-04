package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;

import java.io.IOException;

/**
 * Taps an ImageWithCorners from the stream
 */
public class TapFilter implements Filter<ImageWithCorners, ImageWithCorners> {

    private Object latestLock = new Object();
    private boolean latestWanted = true;
    private ImageWithCorners latest;

    @Override
    public ImageWithCorners filter(ImageWithCorners input) {
        synchronized (latestLock) {
            if (latestWanted) {
                latest = new ImageWithCorners(input);
                latestLock.notifyAll();
            }
        }
        return input;
    }

    private boolean closed = false;

    @Override
    public void close() throws IOException {
        closed = true;
    }

    public ImageWithCorners getMostRecentInput() {
        if (closed) {
            return null;
        }
        synchronized (latestLock) {
            latestWanted = true;
            try {
                latestLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (closed) {
                return null;
            }
            latestWanted = false;
            ImageWithCorners img = latest;
            latest = null;
            return img;
        }
    }
}
