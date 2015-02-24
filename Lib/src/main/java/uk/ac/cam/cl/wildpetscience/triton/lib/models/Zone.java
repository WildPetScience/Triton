package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class Zone implements Serializable {

    public final Box area;
    @SerializedName("zoneName")
    public final String id;
    public final ZoneType zoneType;

    public Zone(Box area, String id) {
        this.area = area;
        this.id = id;
        this.zoneType = new ZoneType(id);
    }

    public Zone(double x, double y, double w, double h, String id) {
        this.area = new Box(x, y, w, h);
        this.id = id;
        this.zoneType = new ZoneType(id);
    }

    public String toString() {
        return "ZONE " + id + " [" + area + "]";
    }

}
