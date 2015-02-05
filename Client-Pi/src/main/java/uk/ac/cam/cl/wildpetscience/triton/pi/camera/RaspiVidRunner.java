package uk.ac.cam.cl.wildpetscience.triton.pi.camera;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Runs raspivid and captures its H.264 output. Performs buffering and frame-
 * dropping to increase performance and reduce latency.
 */
public class RaspiVidRunner {

    private CameraOpts opts;

    private Process process;

    public RaspiVidRunner(CameraOpts opts) {
        this.opts = opts;
    }

    /**
     * Starts the stream
     */
    private synchronized void startStream() throws IOException {
        if (process != null && process.isAlive()) {
            process.destroy();
        }
        process = Runtime.getRuntime().exec(String.format(
                "raspivid -t 0 -w %d -h %d -fps %d -b %d -p 0,0,%d,%d -o -",
                opts.getWidth(),
                opts.getHeight(),
                opts.getFps(),
                opts.getBitRate(),
                opts.getWidth(),
                opts.getHeight()));
        process.getInputStream();
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

    private static class H264Decoder {
        
    }
}
