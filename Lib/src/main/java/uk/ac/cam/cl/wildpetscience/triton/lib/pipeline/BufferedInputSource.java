package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * An ImageInputSource that fetches input ahead of time and buffers input.
 */
public class BufferedInputSource implements ImageInputSource {
    private final ImageInputSource source;
    private final BlockingQueue<Image> queue = new ArrayBlockingQueue<Image>(2);

    private boolean running = false, cancelled = false;

    public BufferedInputSource(ImageInputSource source) {
        this.source = source;
    }

    @Override
    public Image getNext() throws InputFailedException {
        if (!cancelled) {
            if (!running) {
                running = true;
                worker.start();
            }
            try {
                Image img = null;
                while (!cancelled &&
                        (img = queue.poll(1, TimeUnit.SECONDS)) == null);
                return img;
            } catch (InterruptedException e) {
                return null;
            }
        } else { // Cancelled
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        cancelled = true;
        worker.interrupt();
    }

    private final Thread worker = new Thread() {
        @Override
        public void run() {
            while (!cancelled) {
                Image img = null;
                try {
                    img = source.getNext();
                    if (img == null) {
                        cancelled = true;
                        return;
                    }
                    queue.put(img);
                } catch (InputFailedException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // Interrupted, drop image and loop. If cancelled we will
                    // break out.
                    if (img != null) {
                        img.release();
                    }
                }
            }
        }
    };
}
