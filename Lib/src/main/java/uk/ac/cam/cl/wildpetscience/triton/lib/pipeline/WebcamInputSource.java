package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;

import java.io.IOException;

/**
 * Generates live images from a webcam
 */
public class WebcamInputSource implements ImageInputSource {
    private final VideoCapture capture;

    public WebcamInputSource() throws InputFailedException {
        capture = new VideoCapture(1);
        if (!capture.isOpened()) {
            throw new InputFailedException();
        }
    }

    @Override
    public Image getNext() throws InputFailedException {
        Mat mat = new Mat();
        if (!capture.read(mat)) {
            throw new InputFailedException();
        }
        return new Image(mat);
    }

    @Override
    public void close() throws IOException {
        capture.release();
    }
}
