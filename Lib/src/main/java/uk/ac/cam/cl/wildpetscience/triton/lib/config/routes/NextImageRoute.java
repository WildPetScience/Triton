package uk.ac.cam.cl.wildpetscience.triton.lib.config.routes;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.*;

import java.io.IOException;

public class NextImageRoute implements Route {

    private Driver<?> driver;

    public NextImageRoute(Driver<?> driver) {
        this.driver = driver;
    }

    public synchronized void setDriver(Driver<?> driver) {
        this.driver = driver;
    }

    public Object handle(Request request, Response response) {
        Image image;
        synchronized (this) {
            image = driver.getMostRecentInput();
        }
        try {
            response.header("Content-Type", "image/jpeg");
            response.raw().getOutputStream().write(image.toJpeg());
        } catch (IOException e) {
            System.err.println("Error writing image to output stream.");
        }
        image.release();
        return response;
    }

}
