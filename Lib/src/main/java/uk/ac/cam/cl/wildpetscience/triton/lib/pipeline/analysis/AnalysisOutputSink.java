package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis;

import com.google.gson.Gson;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.eclipse.jetty.util.ArrayQueue;
import org.opencv.core.Point;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.*;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.OutputSink;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Performs the analysis stage of the client-side pipeline.
 * This includes:
 *      - Receiving cage and zone data from the config stage.
 *      - Receiving AnimalPosition data from the processing stage.
 *      - Removing anomalies from the location data and replacing with
 *        interpolated data.
 *      - Classifying location data into zones.
 *      - Summing time the animal has spent in each zone.
 *      - Computing an activity metric using the velocity of the animal.
 *      - (Optional: Computing the most common zone transition cycles of
 *        the animal.)
 */
public class AnalysisOutputSink implements OutputSink<AnimalPosition>, Analysis {
    private double cageWidth;
    private double cageHeight;
    private String serverURL;
    private AccessData accessData;
    private String animalType;
    private Set<Zone> zones;
    private AnimalPosition lastKnownPosition;
    private Queue<AnimalPosition> positionQueue;
    private List<PositionDataFrame> path; // NB: for demo only

    private double likelyThreshold = 0.7; // TODO: Experiment with values of likelyThreshold
    private double ignoreThreshold = 0.2; // TODO: Experiment with values of ignoreThreshold
    private int maxDataQueueSize = 5;
    private Queue<PositionDataFrame> dataQueue = new ArrayQueue<>(maxDataQueueSize);

    public AnalysisOutputSink (ConfigData config) {
        onConfigChanged(config);

        lastKnownPosition = null;
        positionQueue = new LinkedList<AnimalPosition>();
        path = new LinkedList<PositionDataFrame>();
    }

    public List<PositionDataFrame> getPath() { return path; } // NB: for demo only

    /* Finds which zone a point is in. NB: assumes zones are disjoint */
    private Zone pointToZone(Point point) {
        for (Zone z : zones) {
            if (z.area.contains(point)) {
                return z;
            }
        }
        /* If no zone found, return the 'N/A' zone */
        return null;
    }

    /* Computes speed between two points in time and space. NB: uses cageWidth and cageHeight */
    private double computeSpeed(Point p1, LocalDateTime t1, Point p2, LocalDateTime t2) {
        double xDiff = Math.abs(p1.x - p2.x) * cageWidth;
        double yDiff = Math.abs(p1.y - p2.y) * cageHeight;
        double displacement = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
        double time = ChronoUnit.MILLIS.between(t1, t2);
        if (time == 0) return 0;
        else return displacement / (time/1000); // time/1000: scale to seconds
    }

    /* Makes a DataFrame object for sending to server. NB: utilises lastKnownPosition */
    public PositionDataFrame makeFrame(AnimalPosition position) {
        LocalDateTime time = position.getTime();
        Point location = position.getLocation();
        double speed = computeSpeed(lastKnownPosition.getLocation(), lastKnownPosition.getTime(), location, time);
        Zone currentZone = pointToZone(location);

        PositionDataFrame frame = new PositionDataFrame(
                time,
                location,
                currentZone,
                speed
        );

        return frame;
    }

    @Override
    public void onConfigChanged(ConfigData config) {
        this.cageWidth = config.getCageWidth();
        this.cageHeight = config.getCageHeight();
        this.serverURL = config.getRemoteServer();
        this.accessData = config.getAccessData();
        this.animalType = config.getAnimalType();

        /* Copy the zone set from config, add the null zone */
        zones = new HashSet<Zone>(config.getZones());

        /* Send updated data to server */
        sendCageData(new CageDataFrame(cageWidth, cageHeight, animalType));
        sendZoneData(new ZoneDataFrame(zones));

    }

    @Override
    public void sendPositionData(PositionDataFrame data) {
        path.add(data); // NB: for demo only
        dataQueue.add(data);

        /* If too much data in queue, flush to server */
        if (dataQueue.size() >= maxDataQueueSize) {
	        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            HttpClient httpClient = HttpClients.createDefault();
            try {
                while (!dataQueue.isEmpty()) {
                    PositionDataFrame frame = dataQueue.poll();
                    System.out.println("Polled from data queue:\n" + frame.toString());
                    HttpPost post = new HttpPost(serverURL + "/api/clients/" + accessData.getIntID() + "/positions?accessKey=" + accessData.accessToken);
                    Gson g = new Gson();

	                HashMap<String, Object> body = new HashMap<>();
	                System.out.println(frame.getTime());
	                body.put("time", f.format(frame.getTime()));
	                body.put("x", frame.getX());
	                body.put("y", frame.getY());
	                body.put("speed", frame.getSpeed());

	                if (frame.getZone() != null) {
		                HashMap<String, String> zone = new HashMap<>();
		                zone.put("zoneName", frame.getZone().id);
		                body.put("zone", zone);
	                }

	                System.out.println(g.toJson(body));
                    StringEntity params = new StringEntity(g.toJson(frame));
	                post.setEntity(params);
                    httpClient.execute(post);
                    post.releaseConnection();
                }
            } catch (HttpHostConnectException e) {
                System.err.println("Failed to connect to server when trying to send position data");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                dataQueue.clear();
            }
        }
    }

    @Override
    public void sendCageData(CageDataFrame data) {
        // TODO: [Nick] Interact with API
        HttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost(serverURL + "/api/clients");
            Gson g = new Gson();
            StringEntity params = new StringEntity(g.toJson(data));
            post.setEntity(params);
            httpClient.execute(post);
            post.releaseConnection();
        } catch (HttpHostConnectException e) {
            System.err.println("Failed to connect to server when trying to send cage data");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Override
    public void sendZoneData(ZoneDataFrame data) {
        // TODO: [Nick] Interact with API
        HttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost(serverURL + "/api/clients/" + accessData.getIntID() + "/zones");
            Gson g = new Gson();
            StringEntity params = new StringEntity(g.toJson(data));
            post.setEntity(params);
            httpClient.execute(post);
            post.releaseConnection();
        } catch (HttpHostConnectException e) {
            System.err.println("Failed to connect to server when trying to send zone data");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Override
    public void onDataAvailable(AnimalPosition image) {
        Point location = image.getLocation();
        LocalDateTime time = image.getTime();
        double probability = image.getProbability();

        /* Ignore data points with too low a probability */
        if (probability < ignoreThreshold) return;

        /* Only start sending when image has settled and position is likely */
        if (lastKnownPosition == null) {
            if (probability > likelyThreshold) {
                lastKnownPosition = image;
                sendPositionData(makeFrame(image));
            }
            return;
        }

        /* Queue position to be processed */
        positionQueue.add(image);

        /* If ACCURATE position provided, interpolate intermediate positions and flush queue */
        if (probability > likelyThreshold) {

            /* The interpolator also flushes the queue */
            List<AnimalPosition> predictedPoints = (new LinearInterpolator()).predictPoints(lastKnownPosition, image, positionQueue);
            for (AnimalPosition prediction : predictedPoints) {
                sendPositionData(makeFrame(prediction));
                lastKnownPosition = prediction;
            }

        }
    }

    @Override
    public void close() throws IOException {

    }

}