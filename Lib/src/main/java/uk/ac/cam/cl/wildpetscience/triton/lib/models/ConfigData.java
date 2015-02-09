package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import java.util.Set;

/**
 * Contains the data produced by the config stage.
 */
public class ConfigData {

    private Set<Zone> zones;
    private double cageWidth;
    private double cageHeight;

    public ConfigData(Set<Zone> zones, double cageWidth, double cageHeight) {
        this.zones = zones;
        this.cageWidth = cageWidth;
        this.cageHeight = cageHeight;
    }

    public Set<Zone> getZones() {
        return zones;
    }

    public double getCageWidth() {
        return cageWidth;
    }

    public double getCageHeight() {
        return cageHeight;
    }

}
