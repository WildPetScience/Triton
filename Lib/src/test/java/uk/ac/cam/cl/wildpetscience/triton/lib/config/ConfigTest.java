package uk.ac.cam.cl.wildpetscience.triton.lib.config;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.*;
import uk.ac.cam.cl.wildpetscience.triton.lib.Bootstrap;

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

    @BeforeClass
    public static void classSetUp() {
        Bootstrap.init();
    }

    @Before
    public void setUp() {
        ConfigServer.stop();
    }

    @AfterClass
    public static void tearDown() {
        ConfigServer.stop();
    }

    @Test
    public void testConfigRouting() throws IOException, InterruptedException {
        Map<String, HTTPRoute> routes = new HashMap<>();
        routes.put("/test1", new HTTPRoute((req, res) -> "CONFIG TEST RESPONSE 1"));
        routes.put("/test2", new HTTPRoute((req, res) -> "CONFIG TEST RESPONSE 2"));
        ConfigServer.start(8123, routes);
        Thread.sleep(400);

        URLConnection conn =
                new URL("http://127.0.0.1:8123/test1").openConnection();
        InputStream content = (InputStream) conn.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        if (!reader.readLine().equals("CONFIG TEST RESPONSE 1")) {
            reader.close();
            throw new RuntimeException(
                    "Spark server returned incorrect response");
        }

        conn = new URL("http://127.0.0.1:8123/test2").openConnection();
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
        ConfigServer.start(8123);
        Thread.sleep(400);

        WebClient webClient = new WebClient();
        HtmlPage page = webClient.getPage("http://127.0.0.1:8123/");
        Assert.assertEquals(page.getTitleText(), "Wild Pet Science Setup");

        //TODO: add more tests here once page layout is finalised
    }

}
