package uk.ac.cam.cl.wildpetscience.triton.pi.camera;

/**
 * Camera options
 */
public class CameraOpts {
    private final int width;
    private final int height;
    private final int delay;
    private final boolean grayscale;

    public CameraOpts(int width, int height, int delay, boolean grayscale) {
        this.width = width;
        this.height = height;
        this.delay = delay;
        this.grayscale = grayscale;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDelay() {
        return delay;
    }

    public boolean isGrayscale() {
        return grayscale;
    }
}
