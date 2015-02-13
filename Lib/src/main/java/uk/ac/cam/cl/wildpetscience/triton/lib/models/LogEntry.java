package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import java.util.Date;

/**
 * Represents an entry in the system log.
 *
 * This class has immutable state as it doesn't make sense to modify
 * the contents of a log entry after it has been logged.
 */
public class LogEntry {

    public final String message;
    public final Date logTime;

    /**
     * Constructs a LogEntry using the current system time.
     *
     * @param m The message to be logged.
     */
    public LogEntry(String m) {
        message = m;
        logTime = new Date();
    }

    /**
     * Constructs a LogEntry object at an arbitrary date / time.
     *
     * @param m The message to be logged.
     * @param d The date / time at which the event occurred.
     */
    public LogEntry(String m, Date d) {
        message = m;
        logTime = d;
    }

}
