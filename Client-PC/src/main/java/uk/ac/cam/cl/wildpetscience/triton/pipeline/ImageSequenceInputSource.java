package uk.ac.cam.cl.wildpetscience.triton.pipeline;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.InputFailedException;

import java.io.File;
import java.io.IOException;
import java.util.function.IntFunction;

/**
 * An ImageInputSource that loads images sequentially from a folder.
 */
public class ImageSequenceInputSource implements ImageInputSource {

    private final IntFunction<File> filenameGenerator;

    public ImageSequenceInputSource(File dir, String filename_format) {
        this(v -> new File(dir, String.format(filename_format, v)));
    }

    public ImageSequenceInputSource(IntFunction<File> filenameGenerator) {
        this.filenameGenerator = filenameGenerator;
    }

    private int pos = 0;

    @Override
    public Image getNext() throws InputFailedException {
        Mat mat = Highgui.imread(filenameGenerator.apply(pos++).getAbsolutePath());
        return new Image(mat);
    }

    @Override
    public void close() throws IOException {

    }
}
