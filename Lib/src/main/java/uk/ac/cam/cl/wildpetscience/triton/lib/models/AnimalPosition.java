package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import org.opencv.core.Point;

import java.time.LocalDateTime;

/**
 * An (location,time,prob) tuple representing the location of the animal at a
 * a particular time and the probability of that location being accurate.
 */
public class AnimalPosition {

    private Point location;
    private LocalDateTime time;
    private double probability;

    public AnimalPosition(Point location, LocalDateTime time, double probability) {
        this.location = location;
        this.time = time;
        this.probability = probability;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
