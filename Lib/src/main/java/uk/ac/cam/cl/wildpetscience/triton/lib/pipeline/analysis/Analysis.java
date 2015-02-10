package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis;

import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.ConfigData;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.DataFrame;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.OutputSink;

import java.util.List;

/**
 * Interface specifying what the analysis engine can be given as input
 */
public interface Analysis extends OutputSink<AnimalPosition> {
    public void setConfigData(ConfigData configData);
    public List<DataFrame> getPath();
}
