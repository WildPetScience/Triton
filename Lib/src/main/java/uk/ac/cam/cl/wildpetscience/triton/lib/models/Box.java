package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import org.opencv.core.Point;

/**
 * Represents a zone of interest within an animal's cage.
 */
public class Box {

    public final Point origin;
    public final Point corner;

    /**
     * Constructs a Box from an origin point, width and height.
     * @param origin
     * @param width
     * @param height
     */
    public Box(Point origin, double width, double height) {
        this.origin = origin;
        this.corner = new Point(origin.x + width, origin.y + height);
    }

    /**
     * Constructs a Box from its two opposing corners.
     * @param origin
     * @param corner
     */
    public Box(Point origin, Point corner) {
        this.origin = origin;
        this.corner = corner;
    }

}
