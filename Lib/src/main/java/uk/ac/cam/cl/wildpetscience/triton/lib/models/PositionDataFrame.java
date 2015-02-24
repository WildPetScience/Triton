package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import org.opencv.core.Point;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * Encapsulates the position data to be sent to the server regularly.
 */

public class PositionDataFrame {

    private LocalDateTime time;
    private Point location;
    private String zoneId;
    private double speed;

    public PositionDataFrame(LocalDateTime time, Point location, String zoneId, double speed) {
        this.time = time;
        this.location = location;
        this.zoneId = zoneId;
        this.speed = speed;
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

    public String toString() {
        DecimalFormat df = new DecimalFormat("#0.00");
        String xString = df.format(location.x);
        String yString = df.format(location.y);
        String timeString = time.format(DateTimeFormatter.ISO_LOCAL_TIME);
        String out =    "Time:          " + timeString + "\n" +
                        "Location:      " + "("+xString+","+yString+")" + "\n" +
                        "Zone ID:       " + zoneId + "\n" +
                        "Speed:         " + df.format(speed) + "\n";
        return out;
    }
}
