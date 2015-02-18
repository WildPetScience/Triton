package uk.ac.cam.cl.wildpetscience.triton.lib;

import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigManager;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigServer;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.*;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis.AnalysisOutputSink;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.processing.CornerDetectionFilter;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.tracker.TrackingFilter;

import java.io.IOException;

/**
 * Main entry point for the lib
 */
public class DefaultApp implements App {
    private final ImageInputSource input;
    private final Filter<Image, Image> preFilter;
    private final int port;

    private Driver<AnimalPosition> driver;

    public DefaultApp(ImageInputSource input, Filter<Image, Image> preFilter, String remoteServer, int port) {
        this.input = input;
        this.port = port;
        this.preFilter = preFilter;
        ConfigManager.setRemoteServer(remoteServer);
        ConfigManager.setApp(this);
    }

    public DefaultApp(ImageInputSource input, int port) {
        this(input, new IdentityFilter<>(), ConfigManager.PUBLIC_ENDPOINT, port);
    }

    public DefaultApp(ImageInputSource input, String remoteServer, int port) {
        this(input, new IdentityFilter<>(), remoteServer, port);
    }

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                stopDriver();

                ConfigServer.stop();
            }
        });
        ConfigServer.start(port);
    }

    public void startDriver() {
        AnalysisOutputSink outputSink = null;
        try {
            outputSink = new AnalysisOutputSink(ConfigManager.getConfigData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConfigManager.addListener(outputSink);
        driver = new Driver<>(
                input,
                preFilter,
                new CornerDetectionFilter(),
                new TrackingFilter(),
                outputSink);

        driver.setKeepInputAlive(true);

        driver.start();

        ConfigServer.setDriver(driver);
    }

    public void stopDriver() {
        ConfigServer.setDriver(new DummyDriver());
        driver.cancel();
        driver = null;
    }
}
