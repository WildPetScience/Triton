package uk.ac.cam.cl.wildpetscience.triton.lib.models;

/**
 * Encapsulates the cage dimensions data to be sent to the server on config change.
 */
public class CageDataFrame {

    private double cageWidth;
    private double cageHeight;

    public CageDataFrame(double cageWidth, double cageHeight) {
        this.cageWidth = cageWidth;
        this.cageHeight = cageHeight;
    }

    public double getCageWidth() {
        return cageWidth;
    }

    public void setCageWidth(double cageWidth) {
        this.cageWidth = cageWidth;
    }

    public double getCageHeight() {
        return cageHeight;
    }

    public void setCageHeight(double cageHeight) {
        this.cageHeight = cageHeight;
    }
}
