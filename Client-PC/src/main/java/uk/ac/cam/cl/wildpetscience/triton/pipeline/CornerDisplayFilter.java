package uk.ac.cam.cl.wildpetscience.triton.pipeline;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;

/**
 * Shows the corners of an ImageWithCorners
 */
public class CornerDisplayFilter implements Filter<ImageWithCorners, Image> {
    @Override
    public Image filter(ImageWithCorners input) {
        Mat dat = input.getData();
        Scalar col = new Scalar(0, 0, 255);
        for (Point point : input.getCorners().get()) {
            Core.circle(dat,
                    new Point(point.x * dat.width(), point.y * dat.height()),
                    10, col, 3);
        }
        return input;
    }

    @Override
    public void close() throws IOException {

    }
}
