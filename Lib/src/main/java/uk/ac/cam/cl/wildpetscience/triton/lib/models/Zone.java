package uk.ac.cam.cl.wildpetscience.triton.lib.models;

/**
 * Represents a zone of interest within an animal's cage.
 */
public class Zone {

    public final Point origin;
    public final Point corner;

    /**
     * Constructs a Zone from an origin point, width and height.
     * @param origin
     * @param width
     * @param height
     */
    public Zone(Point origin, double width, double height) {
        this.origin = origin;
        this.corner = new Point(origin.x + width, origin.y + height);
    }

    /**
     * Constructs a Zone from its two opposing corners.
     * @param origin
     * @param corner
     */
    public Zone(Point origin, Point corner) {
        this.origin = origin;
        this.corner = corner;
    }

}
