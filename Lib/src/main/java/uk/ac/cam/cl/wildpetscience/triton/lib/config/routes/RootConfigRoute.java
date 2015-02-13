package uk.ac.cam.cl.wildpetscience.triton.lib.config.routes;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.mustache.MustacheTemplateEngine;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigManager;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.LogEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the route that should serve the configuration page.
 */
public class RootConfigRoute implements Route {

    public Object handle(Request request, Response response) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("code", ConfigManager.getAccessCode());
        try {
            vars.put("logs", ConfigManager.getSystemLogs());
        } catch(IOException e) {
            List<String> errorList = new ArrayList<>();
            errorList.add("Error retrieving logs.");
            vars.put("logs", errorList);
        }
        return new MustacheTemplateEngine().render(
                new ModelAndView(vars, "index.mustache")
        );
    }

}
