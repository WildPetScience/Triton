package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import org.opencv.core.Point;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Encapsulates the data to be sent to the server.
 */

public class DataFrame {

    private LocalDateTime time;
    private Point location;
    private String zoneId;
    private double speed;
    private double cageWidth;
    private double cageHeight;
    private Map<String, Integer> zoneIdVisits;

    public DataFrame(LocalDateTime time, Point location, String zoneId, double speed, double cageWidth, double cageHeight, Map<String, Integer> zoneIdVisits) {
        this.time = time;
        this.location = location;
        this.zoneId = zoneId;
        this.speed = speed;
        this.cageWidth = cageWidth;
        this.cageHeight = cageHeight;
        this.zoneIdVisits = zoneIdVisits;
    }

}
