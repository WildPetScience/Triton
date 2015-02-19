package uk.ac.cam.cl.wildpetscience.triton.lib.models;

public class Zone {

    public final Box area;
    public final String id;

    public Zone(Box area, String id) {
        this.area = area;
        this.id = id;
    }

    public Zone(double x, double y, double w, double h, String id) {
        this.area = new Box(x, y, w, h);
        this.id = id;
    }

    public String toString() {
        return "ZONE " + id + " [" + area + "]";
    }

}
