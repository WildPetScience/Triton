package uk.ac.cam.cl.wildpetscience.triton.lib.config.routes;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigManager;

import java.io.IOException;

public class StartStopRoute implements Route {

    public Object handle(Request request, Response response) {
        String[] pathSplit = request.pathInfo().split("/");
        String endpoint = pathSplit[pathSplit.length - 1];

        try {
            if (endpoint.equals("start")) {
                ConfigManager.startRecording();
            } else if (endpoint.equals("stop")) {
                ConfigManager.stopRecording();
            }
        } catch(IOException e) {
            e.printStackTrace();
            response.status(500);
            return "";
        }

        response.status(200);
        return "";
    }

}
