package uk.ac.cam.cl.wildpetscience.triton.lib.config.routes;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;

public class NextImageRoute implements Route {

    private ImageInputSource input;

    public Object handle(Request request, Response response) {
        return "";
    }

}
