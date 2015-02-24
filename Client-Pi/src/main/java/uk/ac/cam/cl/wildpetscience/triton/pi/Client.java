package uk.ac.cam.cl.wildpetscience.triton.pi;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.ac.cam.cl.wildpetscience.triton.lib.DefaultApp;
import uk.ac.cam.cl.wildpetscience.triton.lib.Bootstrap;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigManager;
import uk.ac.cam.cl.wildpetscience.triton.pi.camera.CameraAdjustFilter;
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
        opts.addOption("r", "remote", true, "URL of the remote server to make API calls to");

        CommandLine cmd = new GnuParser().parse(opts, args);
        int port = Integer.valueOf(cmd.getOptionValue('p', "8000"));
        String remote = cmd.getOptionValue('r', ConfigManager.getRemoteServer());

        CameraOpts cameraOpts = new CameraOpts(640, 480);

        PiCameraInputSource camera = new PiCameraInputSource(cameraOpts);

        CameraAdjustFilter cameraAdjustFilter = new CameraAdjustFilter(camera);

        DefaultApp app = new DefaultApp(camera, cameraAdjustFilter, remote, port);
        app.start();
    }
}
