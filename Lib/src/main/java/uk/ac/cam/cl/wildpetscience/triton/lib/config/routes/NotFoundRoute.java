package uk.ac.cam.cl.wildpetscience.triton.lib.config.routes;

import spark.Request;
import spark.Response;
import spark.Route;

public class NotFoundRoute implements Route {

    public Object handle(Request request, Response response) {
        return "";
    }

}
