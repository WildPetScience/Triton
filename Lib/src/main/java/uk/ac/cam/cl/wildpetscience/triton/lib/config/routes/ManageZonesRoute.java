package uk.ac.cam.cl.wildpetscience.triton.lib.config.routes;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigManager;

import java.io.IOException;

public class ManageZonesRoute implements Route {

    public Object handle(Request request, Response response) {
        /**
         * On a GET request, we are retrieving the list of current zones
         * as JSON. On a POST request, we are updating the list of system
         * zones to what is being displayed on the canvas.
         */
        Gson gson = new Gson();
        try {
            if (request.requestMethod().equals("GET")) {
                return gson.toJson(ConfigManager.getZones());
            } else if (request.requestMethod().equals("POST")) {
                //TODO: handle updating zones
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
