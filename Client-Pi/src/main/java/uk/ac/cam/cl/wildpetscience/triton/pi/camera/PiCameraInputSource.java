package uk.ac.cam.cl.wildpetscience.triton.pi.camera;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.InputFailedException;

import java.io.IOException;

/**
 * An ImageInputSource that fetches images from a Raspberry Pi camera.
 */
public class PiCameraInputSource implements ImageInputSource, Camera {

    /**
     * The webcam daemon that masquerades the pi cam as a V4L input source.
     */
    protected Process uv4l;

    /**
     * The OpenCV V4L capture interface
     */
    protected VideoCapture videoCapture;

    private CameraOpts opts;

    public PiCameraInputSource(CameraOpts opts) {
        this.opts = opts;

        try {
            uv4l = Runtime.getRuntime().exec(
                    "uv4l -k -f --sched-rr " +
                            "--config-file=/etc/uv4l/uv4l-raspicam.conf " +
                            "--driver raspicam " +
                            "--driver-config-file=/etc/uv4l/uv4l-raspicam.conf");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Wait for uv4l to start
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        videoCapture = new VideoCapture(0);
    }

    @Override
    public synchronized Image getNext() throws InputFailedException {
        Mat mat = new Mat();
        if (!videoCapture.read(mat)) {
            throw new InputFailedException();
        }
        return new Image(mat);
    }

    public void setCameraOpts(CameraOpts opts) {
        this.opts = opts;
    }

    @Override
    public synchronized void close() throws IOException {
        videoCapture.release();
        uv4l.destroy();
        videoCapture = null;
        uv4l = null;
    }
}
