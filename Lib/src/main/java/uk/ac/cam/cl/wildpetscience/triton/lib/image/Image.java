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

    public Image(Mat data) {
        this.data = data;
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
