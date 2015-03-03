package uk.ac.cam.cl.wildpetscience.triton.lib.models;

/**
 * Encapsulates the various access data needed to send to server.
 */
public class AccessData {

    public final String accessToken = "JHsa841fZS91Acv"; // e.g. "FuRrY"
    public final String stringID; // e.g. "whisky cat run"
    private int intID; // e.g. 5

    public AccessData(String stringID) {
        this.stringID = stringID;
        this.intID = -1;
    }

    public int getIntID() {
        return this.intID;
    }

    public void setIntID(int v) {
        this.intID = v;
    }
}