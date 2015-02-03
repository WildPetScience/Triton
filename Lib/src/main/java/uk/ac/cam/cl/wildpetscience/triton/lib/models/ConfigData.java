package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import java.util.Set;

/**
 * Contains the data produced by the config stage.
 */
public class ConfigData {

    public final Set<Zone> zones;

    public ConfigData(Set<Zone> zones) {
        this.zones = zones;
    }

}
