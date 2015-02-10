package uk.ac.cam.cl.wildpetscience.triton.lib;

import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigManager;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigServer;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.ConfigData;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Driver;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis.AnalysisOutputSink;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.processing.CornerDetectionFilter;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.tracker.TrackingFilter;

import java.io.IOException;

/**
 * Main entry point for the lib
 */
public class App {
    private final ImageInputSource input;
    private final int port;

    public App(ImageInputSource input, int port) {
        this.input = input;
        this.port = port;
    }

    public void start() {
        try {
            ConfigData initialConfig = new ConfigData(
                    ConfigManager.getZones(),
                    ConfigManager.getCageWidth(),
                    ConfigManager.getCageHeight());
            Driver<AnimalPosition> driver = new Driver<>(
                    input,
                    new CornerDetectionFilter(),
                    new TrackingFilter(),
                    new AnalysisOutputSink(initialConfig));

            driver.start();

            ConfigServer.start(port, driver);
        } catch(IOException e) {
            System.err.println("Fatal error setting system up.");
            System.exit(1);
        }

    }
}
