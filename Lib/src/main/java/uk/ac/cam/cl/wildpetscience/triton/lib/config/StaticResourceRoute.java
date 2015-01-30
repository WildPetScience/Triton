package uk.ac.cam.cl.wildpetscience.triton.lib.config;

import spark.Request;
import spark.Response;
import spark.Route;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

//TODO: Write tests for this route
//TODO: Improve error handling when copying bytes
//TODO: Get file MIME type to make request info better
//TODO: Have file not found redirect to a 404 page

/**
 * A Route class to serve static resources such as favicons,
 * images and other files. Any files placed in the
 * resources/serve directory will be made available at the URL
 * /file_name when the server is running.
 */
public class StaticResourceRoute implements Route {

    private static final int BUFFER_SIZE = 1024;

    public Object handle(Request request, Response response) {
        String resourcePath = request.pathInfo();
        try {
            File resource = getResource(resourcePath);

            InputStream in = new BufferedInputStream(
                    new FileInputStream(resource));
            OutputStream out = response.raw().getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            while(in.read(buffer) != -1) {
                out.write(buffer);
            }

            in.close();
            out.close();
        } catch(FileNotFoundException e) {
            System.err.println(e.getMessage());
            response.status(404);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return response.raw();
    }

    /**
     * @param resource The name of the resource to be retrieved.
     * @return A File handle to the passed resource.
     * @throws FileNotFoundException
     */
    private File getResource(String resource) throws FileNotFoundException {
        //Adding /serve prevents every resource from being available.
        URL fileURL = this.getClass().getResource("/serve/" + resource);

        if(fileURL == null) {
            throw new FileNotFoundException("The resource " + resource + " could not be found");
        }

        File file;
        try {
            file = new File(fileURL.toURI());
        } catch(URISyntaxException e) {
            file = new File(fileURL.getPath());
        }

        return file;
    }

}
