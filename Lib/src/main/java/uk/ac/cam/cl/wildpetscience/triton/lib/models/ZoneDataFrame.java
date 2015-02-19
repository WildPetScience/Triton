package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import java.util.Set;

/**
 * Encapsulates the zone data to be sent to the server on config change.
 */
public class ZoneDataFrame {

    private Set<Zone> zones;

    public ZoneDataFrame(Set<Zone> zones) {
        this.zones = zones;
    }

    public Set<Zone> getZones() {
        return zones;
    }

    public void setZones(Set<Zone> zones) {
        this.zones = zones;
    }
}
