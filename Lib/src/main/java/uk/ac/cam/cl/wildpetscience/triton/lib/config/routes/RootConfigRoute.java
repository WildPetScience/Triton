package uk.ac.cam.cl.wildpetscience.triton.lib.config.routes;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.mustache.MustacheTemplateEngine;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigManager;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the route that should serve the configuration page.
 */
public class RootConfigRoute implements Route {

    public Object handle(Request request, Response response) {
        Map<String, String> vars = new HashMap<>();
        vars.put("code", ConfigManager.getAccessCode());
        return new MustacheTemplateEngine().render(
                new ModelAndView(vars, "index.mustache")
        );
    }

}
