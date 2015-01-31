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
    private final int delay;

    /**
     * Creates a new ImageSequenceInputSource
     * @param dir The directory where images are stored
     * @param filename_format A format string to be passed to String.format with
     *                        a single integer argument describing the
     *                        filenames.
     * @param delay A delay in milliseconds to add between each picture to
     *              simulate realistic conditions.
     */
    public ImageSequenceInputSource(File dir, String filename_format, int delay) {
        this(v -> new File(dir, String.format(filename_format, v)), delay);
    }

    /**
     * Creates a new ImageSequenceInputSource
     * @param filenameGenerator A function that generates names of files to load
     * @param delay A delay in milliseconds to add between each picture to
     *              simulate realistic conditions.
     */
    public ImageSequenceInputSource(IntFunction<File> filenameGenerator, int delay) {
        this.filenameGenerator = filenameGenerator;
        this.delay = delay;
    }

    private int pos = 0;

    private boolean stopped = false;

    @Override
    public Image getNext() throws InputFailedException {
        if (stopped) return null;

        Mat mat = Highgui.imread(filenameGenerator.apply(pos++).getAbsolutePath());
        if (mat.dataAddr() == 0) { // Decode error, according to javadoc
            return null; // End of stream
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }
        return new Image(mat);
    }

    @Override
    public void close() throws IOException {
        stopped = true;
    }
}
