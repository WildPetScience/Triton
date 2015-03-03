package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.perspectiveTransform;

/**
 * Contains the positions of the 4 detected markers
 */
public class Corners {
    private Point upperLeft;
    private Point lowerLeft;
    private Point upperRight;
    private Point lowerRight;

    public Corners(Point upperLeft, Point upperRight, Point lowerRight, Point lowerLeft) {
        setUpperLeft(upperLeft);
        setLowerLeft(lowerLeft);
        setUpperRight(upperRight);
        setLowerRight(lowerRight);
    }

    public Corners() {
        upperLeft = new Point(0, 0);
        upperRight = new Point(1, 0);
        lowerLeft = new Point(0, 1);
        lowerRight = new Point(1, 1);
    }

    public Corners(Point[] cList) {
        if (cList.length == 4) {
            setUpperLeft(cList[0]);
            setUpperRight(cList[1]);
            setLowerRight(cList[2]);
            setLowerLeft(cList[3]);
        }
    }

    public Point getTransform(Point input) {
        Mat src = new MatOfPoint2f(upperLeft,lowerLeft,upperRight,lowerRight);
        Mat dst = new MatOfPoint2f(new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1));
        Mat transform = Imgproc.getPerspectiveTransform(src, dst);

        MatOfPoint2f in = new MatOfPoint2f(input);
        MatOfPoint2f out = new MatOfPoint2f();
        perspectiveTransform(in, out, transform);

        return out.toArray()[0];
    }

    public Mat transformImage(Mat image) {
        Mat src = new MatOfPoint2f(
                new Point(upperLeft.x * image.width(), upperLeft.y * image.height()),
                new Point(lowerLeft.x * image.width(), lowerLeft.y * image.height()),
                new Point(upperRight.x * image.width(), upperRight.y * image.height()),
                new Point(lowerRight.x * image.width(), lowerRight.y * image.height()));
        Mat dst = new MatOfPoint2f(new Point(0, 0), new Point(0, image.height()), new Point(image.width(), 0), new Point(image.width(), image.height()));
        Mat transform = Imgproc.getPerspectiveTransform(src, dst);

        Mat result = new Mat();
        Imgproc.warpPerspective(image, result, transform, new Size(image.width(), image.height()));
        return result;
    }

    public Point getInverseTransform(Point input) {
        Mat dst = new MatOfPoint2f(upperLeft,lowerLeft,upperRight,lowerRight);
        Mat src = new MatOfPoint2f(new Point(0, 0),  new Point(0, 1), new Point(1, 0), new Point(1, 1));
        Mat transform = Imgproc.getPerspectiveTransform(src, dst);

        MatOfPoint2f in = new MatOfPoint2f(input);
        MatOfPoint2f out = new MatOfPoint2f();
        perspectiveTransform(in, out, transform);

        return out.toArray()[0];
    }

    public Point getLowerLeft() {
        return lowerLeft;
    }

    public void setLowerLeft(Point lowerLeft) {
        this.lowerLeft = lowerLeft;
        this.lowerLeft.x = norm(this.lowerLeft.x);
        this.lowerLeft.y = norm(this.lowerLeft.y);
    }

    public Point getUpperLeft() {
        return upperLeft;
    }

    public void setUpperLeft(Point upperLeft) {
        this.upperLeft = upperLeft;
        this.upperLeft.x = norm(this.upperLeft.x);
        this.upperLeft.y = norm(this.upperLeft.y);
    }

    public Point getLowerRight() {
        return lowerRight;
    }

    public void setLowerRight(Point lowerRight) {
        this.lowerRight = lowerRight;
        this.lowerRight.x = norm(this.lowerRight.x);
        this.lowerRight.y = norm(this.lowerRight.y);
    }

    public Point getUpperRight() {
        return upperRight;
    }

    public void setUpperRight(Point upperRight) {
        this.upperRight = upperRight;
        this.upperRight.x = norm(this.upperRight.x);
        this.upperRight.y = norm(this.upperRight.y);
    }

    public Point[] get() {
        return new Point[] { upperLeft, upperRight, lowerRight, lowerLeft };
    }

    /**
     * Normalises a value
     * @param val
     * @return
     */
    private static double norm(double val) {
        if (val < 0) {
            val = 0;
        }
        if (val > 1) {
            val = 1;
        }
        return val;
    }
}
