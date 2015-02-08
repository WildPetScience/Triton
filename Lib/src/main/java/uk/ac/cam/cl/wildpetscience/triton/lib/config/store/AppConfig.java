package uk.ac.cam.cl.wildpetscience.triton.lib.config.store;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.LogEntry;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zone;

import java.io.*;
import java.util.List;
import java.util.Set;

/**
 * Represents the state of the application's config at a given time.
 * Can be loaded and stored from a JSON file.
 */
public class AppConfig {

    private static final String PRIMARY_CONFIG = "config.json";
    private static final String DEFAULT_CONFIG = "config.default.json";

    private boolean running;
    private String dataCode;
    private List<LogEntry> systemLogs;
    private Set<Zone> zones;

    // Cache the primary config so that we're not accessing the SD card
    // too often (should help with IO performance)
    private static AppConfig primary;

    // Static construction methods & helpers

    /**
     * Gets an instance that corresponds to the application's primary
     * configuration (i.e. the config that the app operates on in
     * normal usage).
     * @return
     * @throws IOException
     */
    public static AppConfig getPrimaryConfig() throws IOException {
        return getConfig(PRIMARY_CONFIG);
    }

    /**
     * Gets an instance that corresponds to the application's default
     * configuration (i.e. the config that the app operates on before
     * it is first configured).
     * @return
     * @throws IOException
     */
    public static AppConfig getDefaultConfig() throws IOException {
        return getConfig(DEFAULT_CONFIG);
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

    // Property getter & setter methods

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

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public List<LogEntry> getSystemLogs() {
        return systemLogs;
    }

    public void setSystemLogs(List<LogEntry> systemLogs) {
        this.systemLogs = systemLogs;
    }

    public Set<Zone> getZones() {
        return zones;
    }

    public void setZones(Set<Zone> zones) {
        this.zones = zones;
    }

}
