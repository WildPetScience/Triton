package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis;

import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigChangedListener;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.*;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.OutputSink;
import java.util.List;

/**
 * Interface specifying what the analysis engine can be given as input
 */
public interface Analysis extends OutputSink<AnimalPosition>, ConfigChangedListener {
    public void onConfigChanged(ConfigData configData);
    public void sendPositionData(PositionDataFrame data);
    public void sendCageData(CageDataFrame data);
    public void sendZoneData(ZoneDataFrame data);
    public List<PositionDataFrame> getPath();
}
