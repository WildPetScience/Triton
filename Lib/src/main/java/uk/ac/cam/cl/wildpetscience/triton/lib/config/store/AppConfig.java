package uk.ac.cam.cl.wildpetscience.triton.lib.config.store;

/**
 * Represents the state of the application's config at a given time.
 * Can be loaded and stored from a JSON file.
 */
public class AppConfig {

    private static final String settingsPath = "";

    private boolean running;

    /**
     * Gets an instance that corresponds to the application's primary
     * configuration (i.e. the config that the app operates on in
     * normal usage).
     * @return
     */
    public static AppConfig getPrimaryConfig() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets this instance as the primary config.
     */
    public static void setAsPrimaryConfig() {
        throw new UnsupportedOperationException();
    }

    /**
     * Constructs a configuration object from a given JSON file.
     * @param configFile
     */
    public AppConfig(String configFile) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return Whether or not the app is running.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @param running
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

}
