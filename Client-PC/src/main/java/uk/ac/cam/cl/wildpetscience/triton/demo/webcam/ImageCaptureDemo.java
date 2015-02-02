package uk.ac.cam.cl.wildpetscience.triton.demo.webcam;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.ac.cam.cl.wildpetscience.triton.demo.VisualPipelineDemo;
import uk.ac.cam.cl.wildpetscience.triton.lib.Bootstrap;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.InputFailedException;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.WebcamInputSource;

/**
 * A demo that shows the output of the webcam.
 */
public class ImageCaptureDemo {
    public static void main(String[] args) throws InputFailedException, ParseException {
        Bootstrap.init();

        Options opts = new Options();
        opts.addOption("w", "webcam", true, "Webcam number to use (default 0)");
        CommandLine cmd = new GnuParser().parse(opts, args);

        int webcam = Integer.valueOf(cmd.getOptionValue('w', "0"));

        VisualPipelineDemo demo = new VisualPipelineDemo(
                new WebcamInputSource(webcam),
                "Live webcam demo");
        demo.start();
    }
}
