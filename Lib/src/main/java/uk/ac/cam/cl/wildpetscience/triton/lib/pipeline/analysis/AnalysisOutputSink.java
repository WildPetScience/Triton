package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis;

import org.opencv.core.Point;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.*;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.OutputSink;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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
    private Map<Zone, Integer> zoneVisits;
    private AnimalPosition lastKnownPosition;
    private Queue<AnimalPosition> positionQueue;
    private double threshold = 0.4; // TODO: Experiment with values of threshold

    public AnalysisOutputSink (ConfigData config) {
        setConfigData(config);

        lastKnownPosition = null;
        positionQueue = new LinkedList<AnimalPosition>();
    }

    /* TODO: Replace this dummy method */
    public void send(DataFrame data) {

    }

    /* Finds which zone a point is in. NB: assumes zones are disjoint */
    public Zone pointToZone(Point point) {
        for (Zone z : zoneVisits.keySet()) {
            if (z.area.contains(point)) {
                return z;
            }
        }
        /* If no zone found, return an ERROR zone encompassing the entire cage */
        return new Zone(new Box(0.5,0.5,1,1), "ERROR");
    }

    /* Computes speed between two points in time and space. NB: uses cageWidth and cageHeight */
    public double computeSpeed(Point p1, LocalDateTime t1, Point p2, LocalDateTime t2) {
        double xDiff = Math.abs(p1.x - p2.x) * cageWidth;
        double yDiff = Math.abs(p1.y - p2.y) * cageHeight;
        double displacement = Math.sqrt(xDiff*xDiff + yDiff*yDiff);
        double time = ChronoUnit.SECONDS.between(t2, t1);
        if (time == 0.0) return 0.0;
        else return displacement / time;
    }

    /* Makes a DataFrame object for sending to server. NB: utilises lastKnownPosition */
    public DataFrame makeFrame(AnimalPosition position) {
        LocalDateTime time = position.getTime();
        Point location = position.getLocation();
        double speed = computeSpeed(lastKnownPosition.getLocation(), lastKnownPosition.getTime(), location, time);
        Zone currentZone = pointToZone(location);

        /* Update visit count for zone */
        int visits = zoneVisits.get(currentZone);
        zoneVisits.put(currentZone, visits+1);

        /* Deep copy the id -> visits map */
        HashMap<String, Integer> zoneIdVisits = new HashMap<String, Integer>(zoneVisits.size());
        for (Zone z : zoneVisits.keySet()) {
            zoneIdVisits.put(new String(z.id), new Integer(zoneVisits.get(z)));
        }

        DataFrame frame = new DataFrame(
                time,
                location,
                currentZone.id,
                speed,
                cageWidth,
                cageHeight,
                zoneIdVisits
        );

        return frame;
    }

    @Override
    public void onDataAvailable(AnimalPosition image) {

        Point location = image.getLocation();
        LocalDateTime time = image.getTime();
        double probability = image.getProbability();

        /* Only start sending when image has settled and position is likely */
        if (lastKnownPosition == null) {
            if (probability > threshold) {
                lastKnownPosition = image;
                send(makeFrame(image));
            }
            return;
        }

        /* Queue position to be processed */
        positionQueue.add(image);

        /* If ACCURATE position provided, linearly interpolate intermediate positions and flush queue */
        if (probability > threshold) {

            /* Calculate average velocity */
            Point lastPoint = lastKnownPosition.getLocation();
            double timeElapsed = ChronoUnit.SECONDS.between(lastKnownPosition.getTime(), time);
            double xRate = (location.x - lastPoint.x) / timeElapsed;
            double yRate = (location.y - lastPoint.y) / timeElapsed;

            /* Update queue position using velocity, flush and send */
            while (!positionQueue.isEmpty()) {
                AnimalPosition position = positionQueue.poll();
                double t = ChronoUnit.SECONDS.between(lastKnownPosition.getTime(), position.getTime());
                double xNew = lastKnownPosition.getLocation().x + xRate * t;
                double yNew = lastKnownPosition.getLocation().y + yRate * t;
                position.setLocation(new Point(xNew, yNew));
                lastKnownPosition = position;
                send(makeFrame(position));
            }

        }
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void setConfigData(ConfigData config) {
        this.cageWidth = config.getCageWidth();
        this.cageHeight = config.getCageHeight();

        // TODO: Elaborate on this method

        /* Initialise the zone -> visit map, also acts as a zone set */
        this.zoneVisits = new HashMap<Zone, Integer>(config.getZones().size());
        for (Zone z : config.getZones()) {
            zoneVisits.put(z, 0);
        }
    }
}
