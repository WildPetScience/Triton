package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import java.util.Set;

/**
 * Information about zones and other info for the analysis engine.
 */
public class Zones {
    private Set<Zone> zones;

    public Zones(Set<Zone> zones) {
        this.zones = zones;
    }

    public Set<Zone> getZones() {
        return zones;
    }

    public void setZones(Set<Zone> zones) {
        this.zones = zones;
    }
}
