package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import org.opencv.core.Point;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    public LocalDateTime getTime() {
        return time;
    }

    public Point getLocation() {
        return location;
    }

    public String getZoneId() {
        return zoneId;
    }

    public double getSpeed() {
        return speed;
    }

    public double getCageWidth() {
        return cageWidth;
    }

    public double getCageHeight() {
        return cageHeight;
    }

    public Map<String, Integer> getZoneIdVisits() {
        return zoneIdVisits;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("#0.00");
        String xString = df.format(location.x*cageWidth);
        String yString = df.format(location.y*cageHeight);
        String timeString = time.format(DateTimeFormatter.ISO_LOCAL_TIME);
        String out =    "Time:          " + timeString + "\n" +
                        "Location:      " + "("+xString+","+yString+")" + "\n" +
                        "Zone ID:       " + zoneId + "\n" +
                        "Speed:         " + df.format(speed) + "\n" +
                        "Cage Size:     " + cageWidth + " x " + cageHeight + "\n";
        return out;
    }
}
