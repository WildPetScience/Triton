package uk.ac.cam.cl.wildpetscience.triton.pi.camera;

/**
 * Camera options
 */
public class CameraOpts {
    private final int width;
    private final int height;
    private final int fps;
    private final int bitRate;

    public CameraOpts(int width, int height, int fps, int bitRate) {
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.bitRate = bitRate;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFps() {
        return fps;
    }

    public int getBitRate() {
        return bitRate;
    }
}
