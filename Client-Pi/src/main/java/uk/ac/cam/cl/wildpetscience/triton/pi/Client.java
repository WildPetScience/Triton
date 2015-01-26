package uk.ac.cam.cl.wildpetscience.triton.pi;

import nu.pattern.OpenCV;

/**
 * Main entry point for Triton running on a Raspberry Pi
 */
public class Client {
    public static void main(String[] args) {
        System.out.println("Stub (on the Pi)");

        // Library initialisation
        OpenCV.loadShared();
    }
}
