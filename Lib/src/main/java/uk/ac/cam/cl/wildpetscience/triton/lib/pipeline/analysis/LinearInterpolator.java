package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis;

import org.opencv.core.Point;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;

import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Modifies intermediates' positions to a straight line between start and end and flushes intermediate queue.
 */
public class LinearInterpolator implements Interpolator {

    @Override
    public List<AnimalPosition> predictPoints(AnimalPosition start, AnimalPosition end, Queue<AnimalPosition> intermediates) {
        LinkedList<AnimalPosition> predicted = new LinkedList<AnimalPosition>();
        Point startLocation = start.getLocation();
        Point endLocation = end.getLocation();

        /* Calculate rates */
        double timeElapsed = ChronoUnit.MILLIS.between(start.getTime(), end.getTime());
        double xRate = 0;
        double yRate = 0;
        if (timeElapsed != 0) { // avoid divide by zero
            xRate = (endLocation.x - startLocation.x) / timeElapsed;
            yRate = (endLocation.y - startLocation.y) / timeElapsed;
        }

        /* Update positions and flush queue*/
        while (!intermediates.isEmpty()) {
            AnimalPosition position = intermediates.poll();
            double t = ChronoUnit.MILLIS.between(start.getTime(), position.getTime());
            double xNew = startLocation.x + xRate * t;
            double yNew = startLocation.y + yRate * t;
            position.setLocation(new Point(xNew, yNew));
            predicted.add(position);
        }

        return predicted;
    }

}
