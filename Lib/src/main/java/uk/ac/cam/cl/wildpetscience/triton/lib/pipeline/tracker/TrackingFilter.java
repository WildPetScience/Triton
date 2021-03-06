package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.tracker;

import org.opencv.core.*;
import org.opencv.imgproc.*;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Corners;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;

import static org.opencv.core.Core.absdiff;
import static org.opencv.imgproc.Imgproc.*;

/**
 * Filter that given an image outputs the position of the animal
 */

public class TrackingFilter implements Filter<ImageWithCorners, AnimalPosition> {
    private int framesBuf = 0;
    private ImageWithCorners prevFrame, currFrame, nextFrame;
    private double xPos, yPos, prob;

    public TrackingFilter() {
        super();
        prevFrame = null;
        currFrame = null;
        nextFrame = null;
        framesBuf = 0;
        xPos = 0;
        yPos = 0;
    }

    public Mat getDiff() {
        Mat prev = prevFrame.getData();
        Mat curr = currFrame.getData();
        Mat next = nextFrame.getData();

        Mat d1 = new Mat(prev.rows(), prev.cols(), CvType.CV_8UC1);
        Mat d2 = new Mat(prev.rows(), prev.cols(), CvType.CV_8UC1);
        Mat result = new Mat(prev.rows(), prev.cols(), CvType.CV_8UC1);

        medianBlur(next, next, 5);

        absdiff(prev, next, d1);
        absdiff(curr, next, d2);
        Imgproc.cvtColor(d1, d1, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(d2, d2, Imgproc.COLOR_BGR2GRAY);

        Core.bitwise_and(d1, d2, result);

        d1.release();
        d2.release();

        threshold(result, result, 15, 255, Imgproc.THRESH_BINARY);

        return result;
    }

    @Override
    public AnimalPosition filter(ImageWithCorners input) {
        if(prevFrame != null)
            prevFrame.release();

        prevFrame = currFrame;
        currFrame = nextFrame;
        nextFrame = input;

        ++framesBuf;
        if(framesBuf > 3)
            framesBuf = 3;

        if(prevFrame == null)
            return new AnimalPosition(new Point(0.0, 0.0), input.getTimestamp(), 0.0);

        if(prevFrame.getData() == null)
            return new AnimalPosition(new Point(0.0, 0.0), input.getTimestamp(), 0.0);

        Mat result = getDiff();

        ClusteringModule.process(result, input.getCorners());

        double xNew = ClusteringModule.getX();
        double yNew = ClusteringModule.getY();
        double probNew = ClusteringModule.getConfidence();

        if(probNew < 0.15) {
            prob = prob - 0.05;
            if(prob < 0.0)
                prob = 0.0;
            return new AnimalPosition(new Point(xPos, yPos), input.getTimestamp(), prob);
        }

        xPos = (xPos + 2*xNew) / 3;
        yPos = (yPos + 2*yNew) / 3;
        prob = probNew;

        result.release();

        Point absolutePos = new Point(xPos, yPos);
        Corners corners = input.getCorners();
        Point relativePos = corners.getTransform(absolutePos);

        if(relativePos.x < 0 || relativePos.y < 0 || relativePos.x > 1 || relativePos.y > 1)
            prob = 0;

        return new AnimalPosition(relativePos, input.getTimestamp(), prob);
    }

    @Override
    public void close() throws IOException {

    }
}
