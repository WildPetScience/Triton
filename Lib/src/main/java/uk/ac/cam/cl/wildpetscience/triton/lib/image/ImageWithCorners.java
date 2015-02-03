package uk.ac.cam.cl.wildpetscience.triton.lib.image;

import org.opencv.core.Mat;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Corners;

/**
 * Image with corners data
 */
public class ImageWithCorners extends Image {

    private Corners corners;

    public ImageWithCorners(Image image, Corners corners) {
        super(image.getData());
        this.corners = corners;
    }

    public Corners getCorners() {
        return corners;
    }

    public void setCorners(Corners corners) {
        this.corners = corners;
    }
}
