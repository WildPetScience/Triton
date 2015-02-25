package uk.ac.cam.cl.wildpetscience.triton.lib.models;

/**
 * Encapsulates the cage dimensions and animal type data to be sent to the server on config change.
 */
public class CageDataFrame {

    private String animalType;
    private double cageWidth;
    private double cageHeight;

    public CageDataFrame(double cageWidth, double cageHeight, String animalType) {
        this.cageWidth = cageWidth;
        this.cageHeight = cageHeight;
        this.animalType = animalType;
    }

    public double getCageWidth() {
        return cageWidth;
    }

    public void setCageWidth(double cageWidth) {
        this.cageWidth = cageWidth;
    }

    public double getCageHeight() {
        return cageHeight;
    }

    public void setCageHeight(double cageHeight) {
        this.cageHeight = cageHeight;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

}
