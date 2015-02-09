package uk.ac.cam.cl.wildpetscience.triton.pi;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.ac.cam.cl.wildpetscience.triton.lib.App;
import uk.ac.cam.cl.wildpetscience.triton.lib.Bootstrap;
import uk.ac.cam.cl.wildpetscience.triton.pi.camera.CameraOpts;
import uk.ac.cam.cl.wildpetscience.triton.pi.camera.PiCameraInputSource;

/**
 * Main entry point for Triton running on a Raspberry Pi.
 */
public class Client {
    public static void main(String[] args) throws ParseException {
        Bootstrap.init();

        Options opts = new Options();
        opts.addOption("p", "port", true, "Port to run webserver on (default 8000)");

        CommandLine cmd = new GnuParser().parse(opts, args);
        int port = Integer.valueOf(cmd.getOptionValue('p', "8000"));

        CameraOpts cameraOpts = new CameraOpts(640, 480, false);

        App app = new App(new PiCameraInputSource(cameraOpts), port);
        app.start();
    }
}
