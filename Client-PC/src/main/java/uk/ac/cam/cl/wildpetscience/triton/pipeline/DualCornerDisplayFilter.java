package uk.ac.cam.cl.wildpetscience.triton.pipeline;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.PassthroughFilter;

import java.io.IOException;

/**
 * Shows the corners of an ImageWithCorners
 */
public class DualCornerDisplayFilter implements Filter<PassthroughFilter.Passthrough<ImageWithCorners,
        ImageWithCorners>, Image> {
    @Override
    public Image filter(PassthroughFilter.Passthrough<ImageWithCorners, ImageWithCorners> input) {
        ImageWithCorners img1 = input.passthrough;
        ImageWithCorners img2 = input.data;
        Mat dat = img2.getData();

        Scalar col = new Scalar(0, 0, 255);
        Scalar col2 = new Scalar(0, 255, 255);
        for (Point point : img1.getCorners().get()) {
            Core.circle(dat,
                    new Point(point.x * dat.width(), point.y * dat.height()),
                    10, col, 3);
        }
        for (Point point : img2.getCorners().get()) {
            Core.circle(dat,
                    new Point(point.x * dat.width(), point.y * dat.height()),
                    10, col2, 3);
        }
        return img2;
    }

    @Override
    public void close() throws IOException {

    }
}
