package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis;

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
    private Set<Zone> zones;
    private Zone nullZone;
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
        return nullZone;
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
                currentZone.id,
                speed
        );

        return frame;
    }

    @Override
    public void onConfigChanged(ConfigData config) {
        this.cageWidth = config.getCageWidth();
        this.cageHeight = config.getCageHeight();
        this.serverURL = config.getRemoteServer();

        /* Copy the zone set from config, add the null zone */
        zones = new HashSet<Zone>(config.getZones());
        nullZone = new Zone(new Box(0,0,0,0), "N/A");
        zones.add(nullZone);

        /* Send updated data to server */
        sendCageData(new CageDataFrame(cageWidth, cageHeight));
        sendZoneData(new ZoneDataFrame(zones));

    }

    @Override
    public void sendPositionData(PositionDataFrame data) {
        System.out.println(data.toString()); // NB: for demo only
        path.add(data); // NB: for demo only
        dataQueue.add(data);

        /* If too much data in queue, flush to server */
        HttpClient httpClient = HttpClients.createDefault(); // httpClient to execute post requests
        if (dataQueue.size() >= maxDataQueueSize) {
            PositionDataFrame frame = dataQueue.poll();

            // TODO: [Nick] Interact with API
            HttpPost post = new HttpPost(serverURL + "/api/clients/$ClientId/api/positions"); // TODO: ClientId?

            /* --- Obviously this doesn't work with using JSON in API, but left as an example --- */
            List<NameValuePair> params = new ArrayList<NameValuePair>(4);
            DecimalFormat df = new DecimalFormat("#0.00");
            String x = df.format(data.getLocation().x);
            String y = df.format(data.getLocation().y);
            String speed = df.format(data.getSpeed());
            params.add(new BasicNameValuePair("x", x));
            params.add(new BasicNameValuePair("y", y));
            params.add(new BasicNameValuePair("time", data.getTime().format(DateTimeFormatter.ISO_LOCAL_TIME)));
            params.add(new BasicNameValuePair("speed", speed));
            try {
                post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            /* --- End of example --- */
        }
    }

    @Override
    public void sendCageData(CageDataFrame data) {
        // TODO: [Nick] Interact with API
    }

    @Override
    public void sendZoneData(ZoneDataFrame data) {
        // TODO: [Nick] Interact with API
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