package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.processing;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;

import java.io.IOException;

import static org.opencv.photo.Photo.fastNlMeansDenoising;

/**
 * Reduce noise in the image captured by the camera.
 */
public class NoiseReductionFilter implements Filter<Image, Image> {
    @Override
    public Image filter(Image input) {
        Mat outputMat = new Mat();
        Mat inputMat = input.getData();
        Mat inputMatGreyscale = new Mat();
        Imgproc.cvtColor(inputMat, inputMatGreyscale, Imgproc.COLOR_RGB2GRAY);
        fastNlMeansDenoising(inputMatGreyscale, outputMat, 5, 13, 21);
        input.release();
        inputMatGreyscale.release();
        Image output = new Image(outputMat);
        return output;
    }

    @Override
    public void close() throws IOException {

    }
}
