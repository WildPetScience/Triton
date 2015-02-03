package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import org.opencv.core.Rect;

public class Zone {

    public final Rect area;
    public final String id;

    public Zone(Rect area, String id) {
        this.area = area;
        this.id = id;
    }

}
