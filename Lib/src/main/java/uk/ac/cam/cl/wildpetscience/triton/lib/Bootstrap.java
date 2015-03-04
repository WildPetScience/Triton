package uk.ac.cam.cl.wildpetscience.triton.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import nu.pattern.OpenCV;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import spark.utils.IOUtils;
import sun.nio.ch.IOUtil;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.CodeGenerator;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigManager;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.store.AppConfig;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.AccessData;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.JoinResponse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * Initialises things that must be done at the start of runtime.
 */

public class Bootstrap {

    public static void init() {
        if (isPi()) {
            // RPi camera V4L module
            System.load("/usr/lib/uv4l/uv4lext/armv6l/libuv4lext.so");
            // OpenCV
            System.load("/usr/lib/jni/libopencv_java249.so");
        } else if (!isWindows()) {
            OpenCV.loadShared();
        } else {
            String arch = is64Bit() ? "x64" : "x86";
            // Assuming static install dir for simplicity
            String installDir = String.format("C:\\opencv\\build\\%s\\vc12\\bin\\", arch);
            String javaDir = String.format("C:\\opencv\\build\\java\\%s\\", arch);

            System.setProperty("java.library.path",
                    System.getProperty("java.library.path") +
                            File.pathSeparator +
                            installDir +
                            File.pathSeparator +
                            javaDir);
            String path = System.getProperty("java.library.path");
            System.load(javaDir + "opencv_java2410.dll");
        }

        // I'm not proud of this code
        try {
            AppConfig primary = AppConfig.getPrimaryConfig();
            primary.saveAsPrimaryConfig();
        } catch(IOException e) {
            // Couldn't get the primary config so see if we can get a default
            try {
                AppConfig def = AppConfig.getDefaultConfig();
                def.setAccessData(
                        new AccessData(
                                new CodeGenerator().nextCode()
                        )
                );
                def.saveAsPrimaryConfig();
            } catch(IOException ee) {
                System.err.println("Problem getting default config.");
                System.err.println("System cannot continue from here.");
                System.exit(1);
            }
        }

        try {
            AppConfig primary = AppConfig.getPrimaryConfig();
            HashMap<String, Object> body = new HashMap<>();
            body.put("cageWidth", primary.getDimensions().getWidth());
            body.put("cageHeight", primary.getDimensions().getHeight());
            body.put("identifier", primary.getAccessData().stringID);
            body.put("accessKey", primary.getAccessData().accessToken);

            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            body.put("dateConnected", f.format(new Date()));
            HashMap<String, String> animalType = new HashMap<>();
            animalType.put("name", primary.getAnimalType());
            body.put("animalType", animalType);

            HttpClient httpClient = HttpClients.createDefault();
            String serverURL = ConfigManager.getRemoteServer();
            HttpPost post = new HttpPost(serverURL + "/api/clients");
            post.setHeader("Content-Type", "application/json");
            Gson g = new Gson();
            StringEntity params = new StringEntity(g.toJson(body));
            post.setEntity(params);
            HttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() != 201) {
                System.out.println("This client is already registered with the server.");
            } else {
                System.out.println("Successfully registered a client.");

                Type t = new TypeToken<JoinResponse>() {}.getType();
                JoinResponse resp = g.fromJson(IOUtils.toString(response.getEntity().getContent()), t);
                ConfigManager.getAccessData().setIntID(Integer.parseInt(resp.dbo.get("_id")));
                ConfigManager.save();
            }
        } catch(IOException e) {
            System.err.println("Primary config does not exist when it should.");
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * Checks if windows. Used for special treatment of OpenCV
     */
    static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    static boolean isPi() {
        return System.getProperty("os.arch").equals("arm");
    }

    static boolean is64Bit() {
        return System.getProperty("sun.arch.data.model").equals("64");
    }
}
