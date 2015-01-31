package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;

import java.io.IOException;

/**
 * Pumps data from an ImageInputSource to an ImageOutputSink.
 */
public class Driver extends Thread {
    private final ImageInputSource in;
    private final ImageOutputSink out;

    private boolean cancelled = false;

    public Driver (ImageInputSource in, ImageOutputSink out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        boolean localCancelled;
        do {
            synchronized (this) {
                localCancelled = cancelled;
            }
            try {
                Image img = in.getNext();
                if (img == null) {
                    out.close();
                    cancel();
                } else {
                    out.onImageAvailable(img);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!localCancelled);
    }

    public synchronized void cancel() {
        cancelled = true;
    }

    public synchronized boolean isCancelled() {
        return cancelled;
    }
}
