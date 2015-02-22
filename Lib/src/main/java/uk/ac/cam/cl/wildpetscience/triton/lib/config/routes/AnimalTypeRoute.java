package uk.ac.cam.cl.wildpetscience.triton.lib.config.routes;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigManager;

import java.io.IOException;

public class AnimalTypeRoute implements Route {

    public Object handle(Request request, Response response) {
        try {
            Gson gson = new Gson();
            if(request.requestMethod().equals("GET")) {
                return gson.toJson(ConfigManager.getAnimal());
            } else if (request.requestMethod().equals("POST")) {
                String animal = gson.fromJson(request.body(), String.class);
                ConfigManager.setAnimal(animal);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
