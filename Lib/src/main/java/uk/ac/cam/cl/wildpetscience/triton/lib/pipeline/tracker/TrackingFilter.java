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
import static org.opencv.imgproc.Imgproc.threshold;

/**
 * Filter that given an image outputs the position of the animal
 */

public class TrackingFilter implements Filter<ImageWithCorners, AnimalPosition> {
    static int framesBuf = 0;
    static ImageWithCorners prevFrame, currFrame, nextFrame;

    public TrackingFilter() {
        super();
        prevFrame = null;
        currFrame = null;
        nextFrame = null;
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
            return new AnimalPosition(new Point(0.0, 0.0), LocalDateTime.now(), 0.0);

        if(prevFrame.getData() == null)
            return new AnimalPosition(new Point(0.0, 0.0), LocalDateTime.now(), 0.0);

        Mat d1 = new Mat();
        Mat d2 = new Mat();
        Mat result = new Mat();

        absdiff(prevFrame.getData(), nextFrame.getData(), d1);
        absdiff(currFrame.getData(), nextFrame.getData(), d2);
        Core.bitwise_and(d1, d2, result);

        threshold(result, result, 30, 255, Imgproc.THRESH_BINARY);

        int count=0;
        double xav = 0.0, yav = 0.0;

        for(int i=0; i<result.rows(); ++i)
            for(int j=0; j<result.cols(); ++j)
                if(result.get(i,j)[0]==255) {
                    xav+=j;
                    yav+=i;
                    ++count;
                }

        double prob = 1000.0 * count / result.rows() / result.cols();
        if(prob > 1.0)
            prob = 1.0;

        if(count == 0)
            return new AnimalPosition(new Point(0.0, 0.0), LocalDateTime.now(), 0.0);

        return new AnimalPosition(new Point(xav/count/result.cols(), yav/count/result.rows()), LocalDateTime.now(), prob);
    }

    @Override
    public void close() throws IOException {

    }
}
