package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import org.opencv.core.Point;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Encapsulates the position data to be sent to the server regularly.
 */

public class PositionDataFrame implements Serializable {

    private LocalDateTime time;
    private double x;
    private double y;
    private double speed;
    @SerializedName("zone")
    private String zoneId;

    public PositionDataFrame(LocalDateTime time, Point location, String zoneId, double speed) {
        this.time = time;
        this.x = location.x;
        this.y = location.y;
        this.zoneId = zoneId;
        this.speed = speed;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Point getLocation() { return new Point(x, y); }

    public String getZoneId() {
        return zoneId;
    }

    public double getSpeed() {
        return speed;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("#0.00");
        String xString = df.format(x);
        String yString = df.format(y);
        String timeString = time.format(DateTimeFormatter.ISO_LOCAL_TIME);
        String out =    "Time:          " + timeString + "\n" +
                        "Location:      " + "("+xString+","+yString+")" + "\n" +
                        "Zone ID:       " + zoneId + "\n" +
                        "Speed:         " + df.format(speed) + "\n";
        return out;
    }
}
