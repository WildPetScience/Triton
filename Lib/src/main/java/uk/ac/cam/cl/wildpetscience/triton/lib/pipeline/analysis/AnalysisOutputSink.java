package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis;

import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zones;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.OutputSink;

import java.io.IOException;

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
    @Override
    public void onDataAvailable(AnimalPosition data) {

    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void setZones(Zones zones) {

    }
}
