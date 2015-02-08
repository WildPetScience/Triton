package uk.ac.cam.cl.wildpetscience.triton.pi.camera;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * Runs raspistill and captures images. Calls a callback with each new image.
 */
public class RaspiStillRunner implements Closeable {
    private CameraOpts opts;

    private final byte[] buf = new byte[4096];

    private Process process;

    public RaspiStillRunner(CameraOpts opts) throws IOException {
        this.opts = opts;
    }

    /**
     * Takes an image using the pi camera
     */
    public synchronized byte[] takeImage() throws IOException, InterruptedException {
        close();
        process = Runtime.getRuntime().exec(String.format(
                "raspistill -t 1 -o - -w %d -h %d -e bmp",
                opts.getWidth(),
                opts.getHeight()));
        InputStream in = process.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        process.waitFor();
        process.destroy();
        byte[] data = out.toByteArray();
        out.close();
        return data;
    }

    public CameraOpts getOpts() {
        return opts;
    }

    /**
     * Sets new camera options. Will cause a brief pause in stream.
     * @param opts
     */
    public synchronized void setOpts(CameraOpts opts) {
        this.opts = opts;
    }

    @Override
    public void close() throws IOException {
        if (process != null && process.isAlive()) {
            process.getInputStream().close();
            process.destroy();
            process = null;
        }
    }
}
