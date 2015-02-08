package uk.ac.cam.cl.wildpetscience.triton.lib.config.store;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Represents the state of the application's config at a given time.
 * Can be loaded and stored from a JSON file.
 */
public class AppConfig {

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
    public void setAsPrimaryConfig() {
        Gson gson = new Gson();
        System.out.println(gson.toJson(this));
    }

    /**
     * Constructs a configuration object from a given JSON file.
     * @param configFile
     */
    public static AppConfig getConfig(String configFileName) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(readConfigFile(configFileName), AppConfig.class);
    }

    private static String readConfigFile(String name) throws IOException {
        String path = AppConfig.class.getResource("/config/" + name).getPath();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        return new String(ByteStreams.toByteArray(in), Charsets.UTF_8);
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
