package uk.ac.cam.cl.wildpetscience.triton.lib.config.routes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigManager;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zone;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

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
                Type zonesType = new TypeToken<List<HashMap<String, String>>>() {}.getType();
                List<HashMap<String, String>> zones = gson.fromJson(request.body(), zonesType);

                Set<Zone> newZones = new HashSet<>();

                for (HashMap<String, String> z : zones) {
                    double x = Double.parseDouble(z.get("x"));
                    double y = Double.parseDouble(z.get("y"));
                    double w = Double.parseDouble(z.get("w"));
                    double h = Double.parseDouble(z.get("h"));
                    String id = z.get("id");
                    Zone zz = new Zone(x, y, w, h, id);
                    System.out.println(zz);
                    newZones.add(zz);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
