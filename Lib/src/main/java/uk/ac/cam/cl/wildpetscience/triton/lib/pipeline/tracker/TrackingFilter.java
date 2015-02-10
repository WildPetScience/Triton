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
    private static double xPos, yPos;

    public TrackingFilter() {
        super();
        prevFrame = null;
        currFrame = null;
        nextFrame = null;
        framesBuf = 0;
        xPos = 0;
        yPos = 0;
    }

    private Mat getDiff() {
        Mat d1 = new Mat();
        Mat d2 = new Mat();
        Mat result = new Mat();

        Mat prev = prevFrame.getData();
        Mat curr = currFrame.getData();
        Mat next = nextFrame.getData();

        medianBlur(next, next, 5);

        absdiff(prev, next, d1);
        absdiff(curr, next, d2);
        Core.bitwise_and(d1, d2, result);

        d1.release();
        d2.release();

        threshold(result, result, 35, 255, Imgproc.THRESH_BINARY);

        return result;
    }

    @Override
    public AnimalPosition filter(ImageWithCorners input) {
        if(prevFrame != null)
            prevFrame.release();

        prevFrame = currFrame;
        currFrame = nextFrame;
        nextFrame = new ImageWithCorners(input);

        ++framesBuf;
        if(framesBuf > 3)
            framesBuf = 3;

        if(prevFrame == null)
            return new AnimalPosition(new Point(0.0, 0.0), input.getTimestamp(), 0.0);

        if(prevFrame.getData() == null)
            return new AnimalPosition(new Point(0.0, 0.0), input.getTimestamp(), 0.0);

        Mat result = getDiff();

        int count=0;
        double xav = 0.0, yav = 0.0;

        for(int i=0; i<result.rows(); ++i)
            for(int j=0; j<result.cols(); ++j)
                if(result.get(i,j)[0]==255) {
                    xav += j;
                    yav += i;
                    ++count;
                }

        double prob = 1000.0 * count / result.rows() / result.cols();
        if(prob > 1.0)
            prob = 1.0;

        if(count == 0)
            return new AnimalPosition(new Point(0.0, 0.0), input.getTimestamp(), 0.0);

        double xNew = xav/count/result.cols();
        double yNew = yav/count/result.rows();

        xPos = (xPos + 2*xNew) / 3;
        yPos = (yPos + 2*yNew) / 3;

        result.release();

        return new AnimalPosition(new Point(xPos, yPos), input.getTimestamp(), prob);
    }

    @Override
    public void close() throws IOException {

    }
}
