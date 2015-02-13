package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import java.util.Set;

/**
 * Contains the data produced by the config stage.
 */
public class ConfigData {

    private Set<Zone> zones;
    private double cageWidth;
    private double cageHeight;
    private String serverURL;

    public ConfigData(Set<Zone> zones, double cageWidth, double cageHeight, String serverURL) {
        this.zones = zones;
        this.cageWidth = cageWidth;
        this.cageHeight = cageHeight;
        this.serverURL = serverURL;
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

    public String getServerURL() {
        return serverURL;
    }
}
