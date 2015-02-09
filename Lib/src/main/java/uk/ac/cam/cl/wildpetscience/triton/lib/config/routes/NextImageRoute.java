package uk.ac.cam.cl.wildpetscience.triton.lib.config.routes;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NextImageRoute implements Route {

    private Driver driver;

    public NextImageRoute(Driver<?> driver) {
        this.driver = driver;
    }

    public Object handle(Request request, Response response) {
        Image image = driver.getMostRecentInput();
        BufferedImage bi = image.toAwtImage();
        try {
            response.header("Content-Type", "image/jpeg");
            ImageIO.write(bi, "JPEG", response.raw().getOutputStream());
        } catch(IOException e) {
            System.err.println("Error writing image to output stream.");
        }
        return response;
    }

}
