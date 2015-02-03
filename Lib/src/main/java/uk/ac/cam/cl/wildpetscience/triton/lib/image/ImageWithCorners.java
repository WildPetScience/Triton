package uk.ac.cam.cl.wildpetscience.triton.lib.image;

import org.opencv.core.Mat;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Corners;

/**
 * Image with corners data
 */
public class ImageWithCorners extends Image {

    private Corners corners;

    /**
     * Creates a new image with corners.
     * @param mat The matrix to use - takes ownership.
     * @param corners
     */
    public ImageWithCorners(Mat mat, Corners corners) {
        super(mat);
        this.corners = corners;
    }

    /**
     * Creates a new image with corners.
     * @param mat The matrix to use - takes ownership
     */
    public ImageWithCorners(Mat mat) {
        super(mat);
        corners = new Corners();
    }

    /**
     * Copy constructor
     * @param image
     * @param corners
     */
    public ImageWithCorners(Image image, Corners corners) {
        this(image.getData().clone(), corners);
    }

    /**
     * Copy constructor
     * @param image
     */
    public ImageWithCorners(Image image) {
        this(image.getData().clone());
    }

    public Corners getCorners() {
        return corners;
    }

    public void setCorners(Corners corners) {
        this.corners = corners;
    }
}
