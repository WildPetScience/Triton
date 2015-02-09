package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis;

import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zones;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.OutputSink;

/**
 * Interface specifying what the analysis engine can be given as input
 */
public interface Analysis extends OutputSink<AnimalPosition> {
    public void setZones(Zones zones);
}
