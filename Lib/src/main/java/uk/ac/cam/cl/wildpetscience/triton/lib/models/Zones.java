package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import java.util.Set;

/**
 * Information about zones and other info for the analysis engine.
 */
public class Zones {
    private Set<Zone> zones;

    private double cageWidth;
    private double cageHeight;

    public Zones(Set<Zone> zones, double cageWidth, double cageHeight) {
        this.zones = zones;
        this.cageHeight = cageHeight;
        this.cageWidth = cageWidth;
    }

    public Set<Zone> getZones() {
        return zones;
    }

    public void setZones(Set<Zone> zones) {
        this.zones = zones;
    }

    public double getCageWidth() {
        return cageWidth;
    }

    public void setCageWidth(double cageWidth) {
        this.cageWidth = cageWidth;
    }

    public double getCageHeight() {
        return cageHeight;
    }

    public void setCageHeight(double cageHeight) {
        this.cageHeight = cageHeight;
    }
}
