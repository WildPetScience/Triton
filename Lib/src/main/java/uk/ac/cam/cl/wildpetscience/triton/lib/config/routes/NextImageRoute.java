package uk.ac.cam.cl.wildpetscience.triton.lib.config.routes;

import org.opencv.core.Mat;
import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.InputFailedException;

public class NextImageRoute implements Route {

    private ImageInputSource input;

    public Object handle(Request request, Response response) {
        try {
            Image image = input.getNext();
            return "";
        } catch(InputFailedException e) {
            response.status(500);
            return "";
        }
    }

}
