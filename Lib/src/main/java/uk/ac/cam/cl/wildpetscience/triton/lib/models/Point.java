package uk.ac.cam.cl.wildpetscience.triton.lib.models;

/**
 * Represents a single point within an animal's cage.
 * Should be between 0.0 and 1.0 (inclusive).
 */
public class Point {

    public final double x;
    public final double y;

    /**
     * @param x
     * @param y
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

}
