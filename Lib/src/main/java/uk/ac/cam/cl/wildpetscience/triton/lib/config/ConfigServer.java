package uk.ac.cam.cl.wildpetscience.triton.lib.config;

import spark.*;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Static class to manage the state of the Spark config server.
 */
public class ConfigServer {

    /**
     * Launches the configuration server if it is not running, configured with a mapping of URLs to routes.
     * Restarts with new routes if the server is already running.
     * @param port
     * @param routes
     */

    public static void start(int port, Map<String, Route> routes) {
        Spark.stop();
        Spark.port(port);
        for(Entry<String, Route> r : routes.entrySet()) {
            Spark.get(r.getKey(), r.getValue());
        }
    }

    /**
     * Starts the server with a sane default configuration.
     * @param port
     */
    public static void start(int port) {
        Map<String, Route> map = new HashMap<>();
        map.put("/config", new RootConfigRoute());
        ConfigServer.start(port, map);
    }

    /**
     * Stops the server if it is running.
     * Has no effect if the server is not running.
     */
    public static void stop() {
        Spark.stop();
    }

    // Private constructor to prevent instantiation
    private ConfigServer() {}

}
