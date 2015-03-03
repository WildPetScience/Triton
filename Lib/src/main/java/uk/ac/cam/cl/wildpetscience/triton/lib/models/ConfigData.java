package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import java.util.Set;

/**
 * Contains the data produced by the config stage.
 */
public class ConfigData {

    private Set<Zone> zones;
    private double cageWidth;
    private double cageHeight;
    private String remoteServer;
    private AccessData accessData;
    private String animalType;

    public ConfigData(Set<Zone> zones, double cageWidth, double cageHeight, String remoteServer, String animalType) {
        this.zones = zones;
        this.cageWidth = cageWidth;
        this.cageHeight = cageHeight;
        this.remoteServer = remoteServer;
        this.accessData = new AccessData("FuRrY", "whisky cat run", 5); // TODO: implement in config
        this.animalType = animalType;
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

    public String getRemoteServer() {
        return remoteServer;
    }

    public AccessData getAccessData() {
        return accessData;
    }

    public String getAnimalType() {
        return animalType;
    }
}