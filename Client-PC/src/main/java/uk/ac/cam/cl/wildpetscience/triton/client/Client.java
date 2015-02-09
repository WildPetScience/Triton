package uk.ac.cam.cl.wildpetscience.triton.client;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.ac.cam.cl.wildpetscience.triton.lib.App;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.InputFailedException;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.WebcamInputSource;

/**
 * Main entry point for Triton running on a computer (not an RPi)
 */
public class Client {
    public static void main(String[] args) throws ParseException, InputFailedException {
        Options opts = new Options();
        opts.addOption("w", "webcam", true, "Webcam number to use (default 0)");
        opts.addOption("p", "port", true, "Port to run webserver on (default 8000)");

        CommandLine cmd = new GnuParser().parse(opts, args);
        int webcam = Integer.valueOf(cmd.getOptionValue('w', "0"));
        int port = Integer.valueOf(cmd.getOptionValue('p', "8000"));

        App app = new App(new WebcamInputSource(webcam), port);
        app.start();
    }
}
