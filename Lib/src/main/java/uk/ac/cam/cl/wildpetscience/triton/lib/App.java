package uk.ac.cam.cl.wildpetscience.triton.lib;

import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigManager;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigServer;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.ConfigData;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Driver;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Filter;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.IdentityFilter;
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
    private final Filter<Image, Image> preFilter;
    private final int port;

    private Driver<AnimalPosition> driver;

    public App(ImageInputSource input, Filter<Image, Image> preFilter, int port) {
        this.input = input;
        this.port = port;
        this.preFilter = preFilter;
    }

    public App(ImageInputSource input, int port) {
        this(input, new IdentityFilter<>(), port);
    }

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                driver.cancel();
                try {
                    // Wait for driver to finish (does lots of killing etc).
                    driver.join(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ConfigServer.stop();
            }
        });
        try {
            ConfigData initialConfig = new ConfigData(
                    ConfigManager.getZones(),
                    ConfigManager.getCageWidth(),
                    ConfigManager.getCageHeight(),
                    ConfigManager.getServerURL());
            driver = new Driver<>(
                    input,
                    preFilter,
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
