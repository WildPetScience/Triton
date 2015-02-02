package uk.ac.cam.cl.wildpetscience.triton.demo.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.ac.cam.cl.wildpetscience.triton.lib.Bootstrap;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.InputFailedException;
import uk.ac.cam.cl.wildpetscience.triton.pipeline.TestVideoChooser;

import java.io.File;

/**
 * A demo that shows the output of the webcam.
 */
public class TestSourceDemo {
    public static void main(String[] args) throws InputFailedException, ParseException {
        Bootstrap.init();

        Options opts = new Options();
        opts.addOption("d", "dir", true, "Path to the TestImageData repo");
        CommandLine cmd = new GnuParser().parse(opts, args);

        String path = cmd.getOptionValue('d', "../TestImageData");

        TestVideoChooser chooser = new TestVideoChooser(new File(path));
        chooser.setVisible(true);
    }
}
