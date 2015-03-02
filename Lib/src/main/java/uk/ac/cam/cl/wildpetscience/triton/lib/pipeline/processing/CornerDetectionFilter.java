package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.processing;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Corners;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

/**
 * Detects corners in an image based on a sample pattern
 */
public class CornerDetectionFilter implements Filter<Image, ImageWithCorners> {

    final static int MARKER_AREA_MIN = 30*20;
    final static int MARKER_AREA_MAX = 400*40;

    double width, height;

    @Override
    public ImageWithCorners filter(Image input) {

        Mat inputMat = input.getData();
        Mat mask = createMask(inputMat);
        width = inputMat.width();
        height = inputMat.height();

        // Get list of corner markers.
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hie = new Mat();
        findContours(mask, contours, hie, RETR_LIST, CHAIN_APPROX_SIMPLE);
        mask.release();
        hie.release();
        ArrayList<MatOfPoint> markers = new ArrayList<>();
        for (MatOfPoint cnt: contours) {
            double area = contourArea(cnt);
            if (MARKER_AREA_MIN < area && area < MARKER_AREA_MAX) {
                markers.add(cnt);
            } else {
                cnt.release();
            }
        }

        Corners imageCorners = findCorners(markers);
        // System.out.println(markers.size() + "#########");

        // Using move semantics, from input to output. input no longer valid.
        ImageWithCorners output;
        if (imageCorners != null) {
            output = new ImageWithCorners(input.getData(), imageCorners);
        } else {
            output = new ImageWithCorners(input.getData());
        }
        return output;
    }

    // Detect corners knowing the position of the markers.
    private Corners findCorners(ArrayList<MatOfPoint> markers) {
        ArrayList<Point> corners = new ArrayList<>();
        for (MatOfPoint mrk : markers) {
            List<Point> list = mrk.toList();
            mrk.release();
            double sumx = 0, sumy = 0;
            for (Point p : list) {
                sumx += p.x;
                sumy += p.y;
            }
            corners.add(new Point(sumx/list.size(), sumy/list.size()));
            // System.out.println(list.size());
        }
        sortCorners(corners);
        // for (Point p : corners) System.out.println(p.x + " " + p.y);
        for (int i = 0; i < corners.size(); i++) {
            corners.get(i).x /= width;
            corners.get(i).y /= height;
        }
        if (corners.size() == 4) {
            return new Corners(corners.get(0), corners.get(1), corners.get(3), corners.get(2));
        }
        return null;
    }

    // Sort corners to make a convex polygon.
    private void sortCorners(ArrayList<Point> corners) {
        Collections.sort(corners, new Comparator<Point>() {
                    public int compare (Point a, Point b) {
                        double val = a.y - b.y;
                        if (val < 0) return -1;
                        else if (val > 0) return 1;
                        else return 0;
                    }
                }
        );
        if (corners.size() > 1) {
            if (corners.get(0).x > corners.get(1).x) {
                Point temp = corners.get(0);
                corners.set(0, corners.get(1));
                corners.set(1, temp);
            }
        }
        if (corners.size() > 3) {
            if (corners.get(2).x > corners.get(3).x) {
                Point temp = corners.get(2);
                corners.set(2, corners.get(3));
                corners.set(3, temp);
            }
        }
    }

    // Binary mask - white for marker, black otherwise.
    public static Mat createMask(Mat inputMat) {
        Mat mask = new Mat();
        // Green colour range.
        // Hue, sat, lum
        // final Scalar lowerb = new Scalar (30, 91, 56);
        // final Scalar upperb = new Scalar (68, 255, 198);
        final Scalar lowerb = new Scalar (30, 0, 0);
        final Scalar upperb = new Scalar (98, 255, 255);
        Mat imgHSV = new Mat();
        cvtColor(inputMat, imgHSV, COLOR_BGR2HSV);
        Core.inRange(imgHSV, lowerb, upperb, mask);
        erode(mask, mask, getStructuringElement(MORPH_ELLIPSE, new Size(5, 5)));
        dilate(mask, mask, getStructuringElement(MORPH_ELLIPSE, new Size(5, 5)));
        dilate(mask, mask, getStructuringElement(MORPH_ELLIPSE, new Size(5, 5)));
        erode(mask, mask, getStructuringElement(MORPH_ELLIPSE, new Size(5, 5)));
        return mask;
    }

    @Override
    public void close() throws IOException {

    }
}