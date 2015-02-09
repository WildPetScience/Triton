package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis;

import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zones;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.OutputSink;

import java.io.IOException;

/**
 * Analyses location data
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
