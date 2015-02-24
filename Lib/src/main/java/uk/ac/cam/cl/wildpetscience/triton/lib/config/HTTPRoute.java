package uk.ac.cam.cl.wildpetscience.triton.lib.config;

import spark.Route;

/**
 * Simple class to associate a Spark route with an HTTP method.
 */
public class HTTPRoute {

    public final Route route;
    public final HTTPMethod method;

    /**
     * Construct an HTTPRoute with a Spark route and an HTTP method.
     * @param route The Spark route to handle requests.
     * @param method The HTTP method to be accepted at the route.
     */
    public HTTPRoute(Route route, HTTPMethod method) {
        this.route = route;
        this.method = method;
    }

    /**
     * Construct an HTTPRoute with a Spark route and HTTP GET as a default.
     * @param route The Spark route to handle requests.
     */
    public HTTPRoute(Route route) {
        this.route = route;
        this.method = HTTPMethod.GET;
    }

}