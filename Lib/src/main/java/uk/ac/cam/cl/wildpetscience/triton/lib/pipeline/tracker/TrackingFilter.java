package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.tracker;

import org.opencv.core.*;
import org.opencv.imgproc.*;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.opencv.core.Core.absdiff;
import static org.opencv.imgproc.Imgproc.*;

/**
 * Filter that given an image outputs the position of the animal
 */

public class TrackingFilter implements Filter<ImageWithCorners, AnimalPosition> {
    private static int framesBuf = 0;
    private static ImageWithCorners prevFrame, currFrame, nextFrame;
    private static double xPos, yPos, prob;

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

        ClusteringModule.Process(result);

        double xNew = ClusteringModule.getX();
        double yNew = ClusteringModule.getY();
        double probNew = ClusteringModule.getConfidence();

        if(probNew < 0.15) {
            prob = prob - 0.05;
            if(prob < 0.0)
                prob = 0.0;
            return new AnimalPosition(new Point(xPos, yPos), input.getTimestamp(), prob);
        }

        xPos = (xPos + xNew*2) / 3;
        yPos = (yPos + yNew*2) / 3;
        prob = probNew;

        result.release();

        return new AnimalPosition(new Point(xPos, yPos), input.getTimestamp(), prob);
    }

    @Override
    public void close() throws IOException {

    }
}
