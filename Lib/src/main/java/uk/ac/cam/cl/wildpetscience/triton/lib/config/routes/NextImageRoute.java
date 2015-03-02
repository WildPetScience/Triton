package uk.ac.cam.cl.wildpetscience.triton.lib.config.routes;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.*;

import java.io.IOException;

public class NextImageRoute implements Route {

    private TapFilter filter;

    public NextImageRoute(TapFilter filter) {
        this.filter = filter;
    }

    public synchronized void setFilter(TapFilter filter) {
        this.filter = filter;
    }

    public Object handle(Request request, Response response) {
        Image image;
        synchronized (this) {
            ImageWithCorners original = filter.getMostRecentInput();
            image = original.getInteriorTransform();
            original.release();
            System.out.println("");
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
