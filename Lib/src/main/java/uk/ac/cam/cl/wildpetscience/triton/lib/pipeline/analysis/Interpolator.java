package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis;

import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;

import java.util.List;
import java.util.Queue;

/**
 * Receives two accurate points and a queue of intermediates.
 * Modifies the positions of the intermediates by making predictions.
 * Flushes the queue and returns a list of predicted points.
 */
public interface Interpolator {
    public List<AnimalPosition> predictPoints(AnimalPosition start, AnimalPosition end, Queue<AnimalPosition> intermediates);
}
