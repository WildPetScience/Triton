package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import org.opencv.core.*;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;

import java.io.IOException;

/**
 * Returns fake images
 */
public class DummyInputSource implements ImageInputSource {
    @Override
    public Image getNext() throws InputFailedException {
        Mat mat = new Mat(480, 640, CvType.CV_8UC3);
        mat.setTo(new Scalar(255, 255, 255));
        Core.line(mat, new Point(0, 0), new Point(640, 480), new Scalar(255, 0, 0));
        Core.line(mat, new Point(640, 0), new Point(0, 480), new Scalar(255, 0, 0));
        return new Image(mat);
    }

    @Override
    public void close() throws IOException {

    }
}
