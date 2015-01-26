package uk.ac.cam.cl.wildpetscience.triton.client;

import nu.pattern.OpenCV;

/**
 * Main entry point for Triton running on a computer (not an RPi)
 */
public class Client {
    public static void main(String[] args) {
        System.out.println("Stub");

        // Library initialisation
        OpenCV.loadShared();
    }
}
