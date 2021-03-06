package uk.ac.cam.cl.wildpetscience.triton.lib.config;

import spark.Route;
import spark.Spark;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.routes.*;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Static class to manage the state of the Spark config server.
 */
public class ConfigServer {

    private static NextImageRoute nextImageRoute;

    /**
     * Launches the configuration server if it is not running, configured with a mapping of URLs to routes.
     * Restarts with new routes if the server is already running.
     * @param port The port to listen on.
     * @param routes A map of URL strings to HTTPRoute objects.
     */
    public static void start(int port, Map<String, HTTPRoute> routes) {
        Spark.stop();
        Spark.port(port);
        for(Entry<String, HTTPRoute> r : routes.entrySet()) {
            String path = r.getKey();
            Route route = r.getValue().route;
            HTTPMethod method = r.getValue().method;
            switch(method) {
                case GET:
                    Spark.get(path, route);
                    break;
                case POST:
                    Spark.post(path, route);
                    break;
                case PUT:
                    Spark.put(path, route);
                    break;
                case PATCH:
                    Spark.patch(path, route);
                    break;
                case DELETE:
                    Spark.delete(path, route);
                    break;
                case OPTIONS:
                    Spark.options(path, route);
                    break;
                case HEAD:
                    Spark.head(path, route);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Starts the server with a sane default configuration.
     * @param port The port to listen on.
     */
    public static void start(int port, Driver driver) {
        Map<String, HTTPRoute> map = new HashMap<>();

        Spark.staticFileLocation("/serve");

        map.put("/", new HTTPRoute(new RootConfigRoute(), HTTPMethod.GET));

        map.put("/start", new HTTPRoute(new StartStopRoute(), HTTPMethod.POST));
        map.put("/stop", new HTTPRoute(new StartStopRoute(), HTTPMethod.POST));

        HTTPRoute route = new HTTPRoute(nextImageRoute = new NextImageRoute(driver.getTapFilter()), HTTPMethod.GET);
        map.put("/image", route);

        map.put("/getzones", new HTTPRoute(new ManageZonesRoute()));
        map.put("/zones", new HTTPRoute(new ManageZonesRoute(), HTTPMethod.POST));

        map.put("/getanimal", new HTTPRoute(new AnimalTypeRoute()));
        map.put("/animal", new HTTPRoute(new AnimalTypeRoute(), HTTPMethod.POST));

        ConfigServer.start(port, map);
    }

    public static void start(int port) {
        Driver driver = new DummyDriver();
        start(port, driver);
    }

    public static void setDriver(Driver<?> driver) {
        if (nextImageRoute != null) {
            nextImageRoute.setFilter(driver.getTapFilter());
        }
    }

    /**
     * Stops the server if it is running.
     * Has no effect if the server is not running.
     */
    public static void stop() {
        Spark.stop();
        nextImageRoute = null;
    }

    // Private constructor to prevent instantiation
    private ConfigServer() {}

}
