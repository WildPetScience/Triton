package uk.ac.cam.cl.wildpetscience.triton.demo.opencv;

import nu.pattern.OpenCV;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.ac.cam.cl.wildpetscience.triton.demo.VisualPipelineDemo;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Driver;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.InputFailedException;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.WebcamInputSource;

/**
 * A demo that shows the output of the webcam.
 */
public class EdgeFilterDemo {
    public static void main(String[] args) throws InputFailedException, ParseException {
        OpenCV.loadShared();

        Options opts = new Options();
        opts.addOption("w", "webcam", true, "Webcam number to use (default 0)");
        CommandLine cmd = new GnuParser().parse(opts, args);

        int webcam = Integer.valueOf(cmd.getOptionValue('w', "0"));
        WebcamInputSource src = new WebcamInputSource(webcam);

        VisualPipelineDemo demo = new VisualPipelineDemo(
                panel -> new Driver<Image>(src,
                        new EdgeFilter(),
                        panel),
                "Live edge detection demo");
        demo.start();
    }
}
