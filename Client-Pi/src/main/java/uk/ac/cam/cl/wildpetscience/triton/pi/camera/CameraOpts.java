package uk.ac.cam.cl.wildpetscience.triton.pi.camera;

/**
 * Camera options
 */
public class CameraOpts {
    private final int width;
    private final int height;
    private final int delay;

    public CameraOpts(int width, int height, int delay) {
        this.width = width;
        this.height = height;
        this.delay = delay;
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
}
