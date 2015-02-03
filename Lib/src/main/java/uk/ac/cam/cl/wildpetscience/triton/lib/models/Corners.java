package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import org.opencv.core.Point;

/**
 * Contains the positions of the 4 detected markers
 */
public class Corners {
    private Point upperLeft;
    private Point lowerLeft;
    private Point upperRight;
    private Point lowerRight;

    public Corners(Point upperLeft, Point lowerLeft, Point upperRight, Point lowerRight) {
        this.upperLeft = upperLeft;
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
        this.lowerRight = lowerRight;
    }

    public Corners() {
        upperLeft = new Point(0, 0);
        upperRight = new Point(1, 0);
        lowerLeft = new Point(0, 1);
        lowerRight = new Point(1, 1);
    }

    public Point getLowerLeft() {
        return lowerLeft;
    }

    public void setLowerLeft(Point lowerLeft) {
        this.lowerLeft = lowerLeft;
    }

    public Point getUpperLeft() {
        return upperLeft;
    }

    public void setUpperLeft(Point upperLeft) {
        this.upperLeft = upperLeft;
    }

    public Point getLowerRight() {
        return lowerRight;
    }

    public void setLowerRight(Point lowerRight) {
        this.lowerRight = lowerRight;
    }

    public Point getUpperRight() {
        return upperRight;
    }

    public void setUpperRight(Point upperRight) {
        this.upperRight = upperRight;
    }

    public Point[] get() {
        return new Point[] { upperLeft, upperRight, lowerRight, lowerLeft };
    }
}
