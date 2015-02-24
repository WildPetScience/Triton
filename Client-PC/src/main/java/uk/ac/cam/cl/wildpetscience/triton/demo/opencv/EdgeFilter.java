package uk.ac.cam.cl.wildpetscience.triton.demo.opencv;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;

/**
 * Demo code for writing a filter. Performs edge detection.
 */
public class EdgeFilter implements Filter<Image, Image> {
    /**
     * Since Image are mutable we have two options:
     * * Create a new image, __and release the old image__
     * * Modify the current image
     * We will go with the first option since this is how OpenCV's pipeline
     * tends to work.
     * @param input a non-null input.
     * @return
     */
    @Override
    public Image filter(Image input) {
        Mat srcGray = new Mat();
        Imgproc.cvtColor(input.getData(), srcGray, Imgproc.COLOR_BGR2GRAY);
        Mat detectedEdges = new Mat();
        Imgproc.blur(srcGray, detectedEdges, new Size(3, 3));
        Imgproc.Canny(detectedEdges, detectedEdges, 100, 300, 3, false);

        Mat dest = new Mat(input.getData().size(), input.getData().type());
        dest.setTo(Scalar.all(0));
        srcGray.copyTo(dest, detectedEdges);

        return new Image(dest);
    }

    @Override
    public void close() throws IOException {

    }
}
