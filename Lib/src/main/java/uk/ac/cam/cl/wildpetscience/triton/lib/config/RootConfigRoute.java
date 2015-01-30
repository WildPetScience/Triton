package uk.ac.cam.cl.wildpetscience.triton.lib.config;

import com.github.mustachejava.MustacheParser;
import spark.*;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

/**
 * This is the route that should serve the configuration page.
 */
public class RootConfigRoute implements Route {

    public Object handle(Request request, Response response) {
        return new MustacheTemplateEngine().render(new ModelAndView(new HashMap<String, String>(), "index.mustache"));
    }

}
