package uk.ac.cam.cl.wildpetscience.triton.lib.config;

import uk.ac.cam.cl.wildpetscience.triton.lib.config.store.AppConfig;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.LogEntry;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zone;

import java.io.IOException;
import java.util.List;
import java.util.Set;

//TODO: decide on method of storing config data

/**
 * Static class to manage the state of a Wild Pet Science installation.
 */
public class ConfigManager {

    /**
     * Start the system - begin to take pictures, process movement and upload data.
     */
    public static void startRecording() {
        System.out.println("Starting the system.");
    }

    /**
     * Stop the system - stop taking pictures and upload any remaining data.
     */
    public static void stopRecording() {
        System.out.println("Stopping the system.");
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
    }

    public static double getCageWidth() throws IOException {
        AppConfig conf = AppConfig.getPrimaryConfig();
        return conf.getDimensions().getWidth();
    }

    public static double getCageHeight() throws IOException {
        AppConfig conf = AppConfig.getPrimaryConfig();
        return conf.getDimensions().getHeight();
    }

    public static void setCageWidth(double width) {
        throw new UnsupportedOperationException();
    }

    public static void setCageHeight(double width) {
        throw new UnsupportedOperationException();
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
    public static List<LogEntry> getSystemLogs() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param entry A new system event to log.
     */
    public static void addLogEntry(LogEntry entry) {
        throw new UnsupportedOperationException();
    }

}
