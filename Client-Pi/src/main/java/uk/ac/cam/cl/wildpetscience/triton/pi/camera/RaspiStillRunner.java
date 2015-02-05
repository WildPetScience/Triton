package uk.ac.cam.cl.wildpetscience.triton.pi.camera;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * Runs raspistill and captures images. Calls a callback with each new image.
 */
public class RaspiStillRunner implements Closeable {
    private CameraOpts opts;
    private final Consumer<BufferedImage> output;

    private Process process;
    private ImageDecoder imageDecoder;

    public RaspiStillRunner(CameraOpts opts, Consumer<BufferedImage> output) {
        this.opts = opts;
        this.output = output;
    }

    /**
     * Starts the stream
     */
    private synchronized void startStream() throws IOException {
        close();
        process = Runtime.getRuntime().exec(String.format(
                "raspistill -t 99999999 -tl 100 -o - -w %d -h %d",
                opts.getWidth(),
                opts.getHeight(),
                opts.getDelay()));
        imageDecoder = new ImageDecoder(process.getInputStream(), output);
        imageDecoder.run();
    }

    private void restartStream() throws IOException {
        startStream();
    }

    public CameraOpts getOpts() {
        return opts;
    }

    /**
     * Sets new camera options. Will cause a brief pause in stream.
     * @param opts
     */
    public void setOpts(CameraOpts opts) throws IOException {
        this.opts = opts;
        restartStream();
    }

    @Override
    public void close() throws IOException {
        if (process != null && process.isAlive()) {
            process.getInputStream().close();
            process.destroy();
            process = null;
        }
        if (imageDecoder != null) {
            imageDecoder.cancel();
            imageDecoder = null;
        }
    }

    private static class ImageDecoder extends Thread {

        private boolean cancelled = false;
        private final InputStream input;
        private final Consumer<BufferedImage> output;

        public ImageDecoder(InputStream input, Consumer<BufferedImage> output) {
            this.input = input;
            this.output = output;
        }

        @Override
        public void run() {
            boolean localCancelled;
            do {
                try {
                    BufferedImage img = ImageIO.read(input);
                    output.accept(img);
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        input.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    // Stop capturing when a failure happens
                    break;
                }

                synchronized (this) {
                    localCancelled = cancelled;
                }
            } while (!localCancelled);
        }

        public void cancel() {
            cancelled = true;
        }
    }
}
