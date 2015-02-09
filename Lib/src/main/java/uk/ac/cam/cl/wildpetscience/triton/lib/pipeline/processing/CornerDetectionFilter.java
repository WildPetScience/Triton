package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.processing;

import org.opencv.core.*;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Corners;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

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

    final static int MARKER_AREA_MIN = 300;
    final static int MARKER_AREA_MAX = 500;
    @Override
    public ImageWithCorners filter(Image input) {
        Mat inputMat = input.getData();
        Mat mask = createMask(inputMat);
        Mat maskCopy = mask.clone();

        // Get list of corner markers.
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hie = new Mat();
        findContours(mask, contours, hie, RETR_LIST, CHAIN_APPROX_SIMPLE);
        ArrayList<MatOfPoint> markers = new ArrayList<>();
        for (MatOfPoint cnt: contours) {
            double area = contourArea(cnt);
            if (MARKER_AREA_MIN < area && area < MARKER_AREA_MAX) {
                markers.add(cnt);
            }
        }

        Corners imageCorners = findCorners(markers);

        ImageWithCorners output;
        if (imageCorners != null) {
            output = new ImageWithCorners(input, imageCorners);
        } else {
            output = new ImageWithCorners(input);
        }
        input.release();
        return output;
    }

    // Detect corners knowing the position of the markers.
    private Corners findCorners(ArrayList<MatOfPoint> markers) {
        ArrayList<Point> corners = new ArrayList<>();
        for (MatOfPoint mrk : markers) {
            List<Point> list = mrk.toList();
            double sumx = 0, sumy = 0;
            for (Point p : list) {
                sumx += p.x;
                sumy += p.y;
            }
            corners.add(new Point(sumx/list.size(), sumy/list.size()));
            System.out.println(list.size());
        }
        for (Point p : corners) System.out.println(p.x + " " + p.y);
        sortCorners(corners);
        if (corners.size() > 3) {
            return new Corners(corners.get(0), corners.get(1), corners.get(2), corners.get(3));
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
    private Mat createMask(Mat inputMat) {
        Mat mask = new Mat();
        // Green colour range.
        final Scalar lowerb = new Scalar (50,60,60);
        final Scalar upperb = new Scalar (70,255,255);
        Mat imgHSV = new Mat();
        cvtColor(inputMat, imgHSV, COLOR_BGR2HSV);
        Core.inRange(imgHSV, lowerb, upperb, mask);
        erode(mask, mask, getStructuringElement(MORPH_ELLIPSE, new Size(5, 5)));
        dilate(mask, mask, getStructuringElement(MORPH_ELLIPSE, new Size(5, 5)));
        return mask;
    }

    @Override
    public void close() throws IOException {

    }
}
