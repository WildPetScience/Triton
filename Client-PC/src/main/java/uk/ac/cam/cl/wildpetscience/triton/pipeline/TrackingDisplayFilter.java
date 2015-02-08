package uk.ac.cam.cl.wildpetscience.triton.pipeline;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.PassthroughFilter;

import java.io.IOException;

/**
 * Shows the corners of an ImageWithCorners
 */
public class TrackingDisplayFilter implements
        Filter<PassthroughFilter.Passthrough<ImageWithCorners, AnimalPosition>, Image> {

    private CornerDisplayFilter cornerDisplayFilter = new CornerDisplayFilter();

    @Override
    public Image filter(PassthroughFilter.Passthrough<ImageWithCorners, AnimalPosition> input) {
        Image corners = cornerDisplayFilter.filter(input.passthrough);

        Scalar colour = new Scalar(255 * (1-input.data.getProbability()),
                255 * input.data.getProbability(),
                0);

        Core.circle(corners.getData(),
                new Point(input.data.getLocation().x * corners.getData().width(),
                        input.data.getLocation().y * corners.getData().height()),
                13, colour, 3);
        return corners;
    }

    @Override
    public void close() throws IOException {
        cornerDisplayFilter.close();
    }
}
