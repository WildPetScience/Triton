package uk.ac.cam.cl.wildpetscience.triton.pi.camera;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.InputFailedException;

import java.io.IOException;

/**
 * An ImageInputSource that fetches images from a Raspberry Pi camera.
 */
public class PiCameraInputSource implements ImageInputSource {

    private RaspiStillRunner runner = null;

    private CameraOpts opts;

    public PiCameraInputSource(CameraOpts opts) {
        this.opts = opts;
        try {
            runner = new RaspiStillRunner(opts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Image getNext() throws InputFailedException {
        if (runner == null) {
            return null;
        }
        try {
            MatOfByte data = new MatOfByte(runner.takeImage());
            Mat img = Highgui.imdecode(data, opts.isGrayscale() ?
                    Highgui.CV_LOAD_IMAGE_GRAYSCALE :
                    Highgui.CV_LOAD_IMAGE_COLOR);
            return new Image(img);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setCameraOpts(CameraOpts opts) {
        runner.setOpts(opts);
    }

    @Override
    public void close() throws IOException {
        if (runner != null) {
            runner.close();
        }
    }
}
