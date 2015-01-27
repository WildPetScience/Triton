package uk.ac.cam.cl.wildpetscience.triton.lib.spark;

import org.junit.Test;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Tests that Spark is present and working.
 */
public class SparkTest {
    @Test
    public void testLoadSpark() throws IOException, InterruptedException {
        Spark.setPort(9877);
        Spark.get(new Route("/get") {
            @Override
            public Object handle(Request request, Response response) {
                return "Spark server";
            }
        });
        // Wait for spark to be up
        Thread.sleep(400);
        URLConnection conn =
                new URL("http://127.0.0.1:9877/get").openConnection();
        InputStream content = (InputStream) conn.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        if (!reader.readLine().equals("Spark server")) {
            reader.close();
            throw new RuntimeException(
                    "Spark server returned incorrect response");
        }
        reader.close();
    }
}