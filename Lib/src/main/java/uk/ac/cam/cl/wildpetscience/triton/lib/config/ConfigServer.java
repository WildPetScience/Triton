package uk.ac.cam.cl.wildpetscience.triton.lib.config;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Static class to manage the state of the Spark config server.
 */
public class ConfigServer {

    private static boolean serverRunning = false;

    /**
     * Launches the configuration server if it is not running, configured with a mapping of URLs to routes.
     * Has no effect if the server is already running.
     * @param port
     * @param routes
     */
    public static void start(int port, Map<String, Route> routes) {
        if(!serverRunning) {
            Spark.port(port);
            for(Entry<String, Route> r : routes.entrySet()) {
                Spark.get(r.getKey(), r.getValue());
            }
            serverRunning = true;
        }
    }

    /**
     * Starts the server with a sane default configuration.
     * @param port
     */
    public static void start(int port) {
        Map<String, Route> map = new HashMap<>();
        map.put("/", new RootConfigRoute());
        start(port, map);
    }

    /**
     * Stops the server if it is running.
     * Has no effect if the server is not running.
     */
    public static void stop() {
        if(serverRunning) {
            Spark.stop();
            serverRunning = false;
        }
    }

    // Private constructor to prevent instantiation
    private ConfigServer() {}

}
