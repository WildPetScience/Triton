package uk.ac.cam.cl.wildpetscience.triton.lib.image;

import org.opencv.core.Mat;

/**
 * An image
 */
public class Image {
    /**
     * The image data encoded in an OpenCV Mat
     */
    private Mat data;

    public Image() {
        data = new Mat();
    }

    public Image(Mat data) {
        this.data = data;
    }

    /**
     * Copy constructor - copies the Mat inside the Image
     * @param img
     */
    public Image(Image img) {
        this(img.getData().clone());
    }

    public Mat getData() {
        return data;
    }

    public void release() {
        if (data != null) {
            data.release();
            data = null;
        }
    }
}
