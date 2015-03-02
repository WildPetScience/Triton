package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.processing;

import org.opencv.core.Point;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Corners;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;
import java.util.*;

/**
 * Normalise corners.
 */

public class CornerNormalisationFilter implements Filter<ImageWithCorners, ImageWithCorners> {

    Deque<Corners> queue = new LinkedList<>();

    final static int MAX_SIZE = 10;
    final static double THRESH = 0.1;

    @Override
    public ImageWithCorners filter(ImageWithCorners input) {
        if (!input.isValid()) {

            ImageWithCorners output;
            if (queue.size() > 0) {
                output = new ImageWithCorners(input, queue.getFirst());
            } else {
                output = new ImageWithCorners(input);
            }

            input.release();

            return output;
        }

        // Check if one of the corners has changed its position too much.
        if (queue.size() > 0) {
            double dxmax = -2, dymax = -2, dxmin = 2, dymin = 2;
            Point[] prevC = queue.getFirst().get();
            Point[] currC = input.getCorners().get();
            if (prevC.length == 4) {
                for (int i = 0; i < 4; i++) {
                    dxmax = max(dxmax, currC[i].x - prevC[i].x);
                    dymax = max(dymax, currC[i].y - prevC[i].y);
                    dxmin = min(dxmin, currC[i].x - prevC[i].x);
                    dymin = min(dymin, currC[i].y - prevC[i].y);
                }
            }
            // System.out.println(dxmin + " " + dymin + "   " + dxmax + " " + dymax);
            if (abs(dxmax - dxmin) > THRESH || abs(dymax - dymin) > THRESH) {
                ImageWithCorners output = new ImageWithCorners(input, queue.getFirst());
                input.release();
                return output;
            }

            queue.add(input.getCorners());
            if (queue.size() >= MAX_SIZE) {
                queue.remove();
            }

            if (abs(dxmax) < 0.05 && abs(dymax) < 0.05) {
                ImageWithCorners output = new ImageWithCorners(input, queue.getFirst());
                input.release();
                return output;
            }
        }

        queue.add(input.getCorners());
        if (queue.size() >= MAX_SIZE) {
            queue.remove();
        }

        Corners cor = input.getCorners();
        ImageWithCorners output = new ImageWithCorners(input, cor);
        input.release();
        return output;
    }

    private double abs(double x) {
        if (x < 0) x = -x;
        return x;
    }

    @Override
    public void close() throws IOException {

    }

    private double max(double x, double y) {
        if (x < y) x = y;
        return x;
    }

    private double min(double x, double y) {
        if (x > y) x = y;
        return x;
    }
}


