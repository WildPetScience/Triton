package uk.ac.cam.cl.wildpetscience.triton.lib.config;

import uk.ac.cam.cl.wildpetscience.triton.lib.App;
import uk.ac.cam.cl.wildpetscience.triton.lib.DummyApp;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.store.AppConfig;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Box;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.ConfigData;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.LogEntry;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zone;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Static class to manage the state of a Wild Pet Science installation.
 */
public class ConfigManager {
    private static List<WeakReference<ConfigChangedListener>> listeners =
            new ArrayList<>();

    private static App app = new DummyApp();

    public static void setApp(App app) {
        ConfigManager.app = app;
    }

    /**
     * Start the system - begin to take pictures, process movement and upload data.
     */
    public static void startRecording() {
        System.out.println("Starting the system.");
        app.startDriver();
    }

    /**
     * Stop the system - stop taking pictures and upload any remaining data.
     */
    public static void stopRecording() {
        System.out.println("Stopping the system.");
        app.stopDriver();
    }

    /**
     * @return a list of interesting zones registered with the system.
     */
    public static Set<Zone> getZones() throws IOException {
        AppConfig conf = AppConfig.getPrimaryConfig();
        return conf.getZones();
    }

    /**
     * Updates the interesting zones registered with the system.
     * @param zones
     */
    public static void setZones(Set<Zone> zones) throws IOException {
        AppConfig conf = AppConfig.getPrimaryConfig();
        conf.setZones(zones);
        conf.saveAsPrimaryConfig();
        broadcastToListeners();
    }

    public static double getCageWidth() throws IOException {
        AppConfig conf = AppConfig.getPrimaryConfig();
        return conf.getDimensions().getWidth();
    }

    public static double getCageHeight() throws IOException {
        AppConfig conf = AppConfig.getPrimaryConfig();
        return conf.getDimensions().getHeight();
    }

    public static String getAnimal() throws IOException {
        AppConfig conf = AppConfig.getPrimaryConfig();
        return conf.getAnimalType();
    }

    public static void setAnimal(String type) throws IOException {
        AppConfig conf = AppConfig.getPrimaryConfig();
        conf.setAnimalType(type);
        conf.saveAsPrimaryConfig();
    }

    public static void setCageWidth(double width) throws IOException {
        AppConfig conf = AppConfig.getPrimaryConfig();
        Box dimensions = conf.getDimensions();
        dimensions.setWidth(width);
        conf.setDimensions(dimensions);
        conf.saveAsPrimaryConfig();
        broadcastToListeners();
    }

    public static void setCageHeight(double height) throws IOException {
        AppConfig conf = AppConfig.getPrimaryConfig();
        Box dimensions = conf.getDimensions();
        dimensions.setHeight(height);
        conf.setDimensions(dimensions);
        conf.saveAsPrimaryConfig();
        broadcastToListeners();
    }

    public static final String PUBLIC_ENDPOINT = "https://wps-condor.herokuapp.com/condor";

    private static String remoteServer = PUBLIC_ENDPOINT;

    /**
     * Sets the remote server. This value isn't persisted since it is set as a
     * command line parameter
     * @param remoteServer
     */
    public static void setRemoteServer(String remoteServer) {
        ConfigManager.remoteServer = remoteServer;
    }

    public static String getRemoteServer() {
        return remoteServer;
    }

    /**
     * @return The unique, random access code for viewing data online. Null if the primary
     * config cannot be found.
     */
    public static String getAccessCode() {
        try {
            AppConfig conf = AppConfig.getPrimaryConfig();
            return conf.getDataCode();
        } catch(IOException e) {
            System.err.println("No primary config found when getting code.");
            return null;
        }
    }

    /**
     * @return A List of logged system events.
     */
    public static List<LogEntry> getSystemLogs() throws IOException {
        AppConfig conf = AppConfig.getPrimaryConfig();
        return conf.getSystemLogs();
    }

    /**
     * @param entry A new system event to log.
     */
    public static void addLogEntry(LogEntry entry) throws IOException {
        AppConfig conf = AppConfig.getPrimaryConfig();
        List<LogEntry> logs = conf.getSystemLogs();
        logs.add(entry);
        conf.saveAsPrimaryConfig();
    }

    public static ConfigData getConfigData() throws IOException {
        return new ConfigData(getZones(), getCageWidth(), getCageHeight(), remoteServer, getAnimal());
    }

    private static void broadcastToListeners() {
        Iterator<WeakReference<ConfigChangedListener>> it = listeners.iterator();
        ConfigData data;
        try {
            data = getConfigData();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        while (it.hasNext()) {
            ConfigChangedListener listener = it.next().get();
            if (listener == null) {
                it.remove();
                continue;
            }
            listener.onConfigChanged(data);
        }
    }

    public static void addListener(ConfigChangedListener listener) {
        listeners.add(new WeakReference<>(listener));
    }
}
