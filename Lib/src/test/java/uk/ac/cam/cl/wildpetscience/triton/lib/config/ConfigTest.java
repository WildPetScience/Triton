package uk.ac.cam.cl.wildpetscience.triton.lib.config;

import org.junit.AfterClass;
import org.junit.Test;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;

import javax.print.DocFlavor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests the usage of the ConfigServer.
 */
public class ConfigTest {

    @AfterClass
    public static void tearDown() {
        ConfigServer.stop();
    }

    @Test
    public void testConfigRouting() throws IOException, InterruptedException {
        Map<String, Route> routes = new HashMap<>();
        routes.put("/test1", (req, res) -> "CONFIG TEST RESPONSE 1");
        routes.put("/test2", (req, res) -> "CONFIG TEST RESPONSE 2");
        ConfigServer.start(8000, routes);
        Thread.sleep(400);

        URLConnection conn =
                new URL("http://127.0.0.1:8000/test1").openConnection();
        InputStream content = (InputStream) conn.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        if (!reader.readLine().equals("CONFIG TEST RESPONSE 1")) {
            reader.close();
            throw new RuntimeException(
                    "Spark server returned incorrect response");
        }

        conn = new URL("http://127.0.0.1:8000/test2").openConnection();
        content = (InputStream) conn.getContent();
        reader = new BufferedReader(new InputStreamReader(content));
        if (!reader.readLine().equals("CONFIG TEST RESPONSE 2")) {
            reader.close();
            throw new RuntimeException(
                    "Spark server returned incorrect response");
        }

        reader.close();
    }

    @Test
    public void testIndexPage() throws IOException, InterruptedException {
        ConfigServer.start(8001);
        Thread.sleep(400);

        URLConnection conn = new URL("http://127.0.0.1:8001/").openConnection();
        InputStream content = (InputStream) conn.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        String r;
        while((r = reader.readLine()) != null) {
            System.out.println(r);
        }
    }

}
