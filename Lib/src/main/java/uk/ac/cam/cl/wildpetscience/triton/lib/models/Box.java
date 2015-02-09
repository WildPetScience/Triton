package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import org.opencv.core.Point;

/**
 * Box class
 */
public class Box {
    private Point centre;
    private double width, height;

    public Box(Point centre, double width, double height) {
        this.centre = centre;
        this.width = width;
        this.height = height;
    }

    public Box(double x, double y, double width, double height) {
        this.centre = new Point(x, y);
        this.width = width;
        this.height = height;
    }

    public boolean contains(Point point) {
        return
                point.x <= centre.x + width / 2.0 &&
                point.x >= centre.x - width / 2.0 &&
                point.y <= centre.y + height / 2.0 &&
                point.y >= centre.y - height / 2.0;
    }

    public Point getCentre() {
        return centre;
    }

    public void setCentre(Point centre) {
        this.centre = centre;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setX(double x) {
        centre.x = x;
    }

    public void setY(double y) {
        centre.y = y;
    }

    public double getX() {
        return centre.x;
    }

    public double getY() {
        return centre.y;
    }

    public Box scale(double xFactor, double yFactor) {
        return new Box(centre.x * xFactor,
                centre.y * yFactor,
                width * xFactor,
                height * yFactor);
    }
}
