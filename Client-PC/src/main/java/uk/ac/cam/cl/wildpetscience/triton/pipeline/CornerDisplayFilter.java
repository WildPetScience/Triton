package uk.ac.cam.cl.wildpetscience.triton.pipeline;

import org.opencv.core.*;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;

/**
 * Shows the corners of an ImageWithCorners
 */
public class CornerDisplayFilter implements Filter<ImageWithCorners, ImageWithCorners> {
    @Override
    public ImageWithCorners filter(ImageWithCorners input) {
        Mat dat = input.getData();
        Scalar col = new Scalar(0, 0, 255);
        int i = 0;
        for (Point point : input.getCorners().get()) {
            Core.circle(dat,
                    new Point(point.x * dat.width(), point.y * dat.height()),
                    10, col, 3);
            Core.putText(dat, "" + i++,
                    new Point(point.x * dat.width() + 20, point.y * dat.height() - 20),
                    Core.FONT_HERSHEY_COMPLEX,1, new Scalar(0, 0, 255));
        }
        return input;
    }

    @Override
    public void close() throws IOException {

    }
}
