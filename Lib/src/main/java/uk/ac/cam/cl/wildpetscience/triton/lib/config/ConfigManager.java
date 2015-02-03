package uk.ac.cam.cl.wildpetscience.triton.lib.config;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.LogEntry;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zone;

import java.util.List;

//TODO: decide on method of storing config data

/**
 * Static class to manage the state of a Wild Pet Science installation.
 */
public class ConfigManager {

    /**
     * Start the system - begin to take pictures, process movement and upload data.
     */
    public static void startRecording() {
        throw new UnsupportedOperationException();
    }

    /**
     * Stop the system - stop taking pictures and upload any remaining data.
     */
    public static void stopRecording() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return a list of interesting zones registered with the system.
     */
    public static List<Zone> getZones() {
        throw new UnsupportedOperationException();
    }

    /**
     * Updates the interesting zones registered with the system.
     * @param zones
     */
    public static void setZones(List<Zone> zones) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return The unique, random access code for viewing data online.
     */
    public static String getAccessCode() {
        throw new UnsupportedOperationException();
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
