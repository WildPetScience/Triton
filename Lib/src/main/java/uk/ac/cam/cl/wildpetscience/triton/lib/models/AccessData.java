package uk.ac.cam.cl.wildpetscience.triton.lib.models;

/**
 * Encapsulates the various access data needed to send to server.
 */
public class AccessData {

    public final String accessToken; // e.g. "FuRrY"
    public final String stringID; // e.g. "whisky cat run"
    public final int intID; // e.g. 5

    public AccessData(String accessToken, String stringID, int intID) {
        this.accessToken = accessToken;
        this.stringID = stringID;
        this.intID = intID;
    }
}