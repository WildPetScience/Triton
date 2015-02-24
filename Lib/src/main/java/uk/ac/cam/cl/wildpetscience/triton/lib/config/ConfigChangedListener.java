package uk.ac.cam.cl.wildpetscience.triton.lib.config;

import uk.ac.cam.cl.wildpetscience.triton.lib.models.ConfigData;

public interface ConfigChangedListener {
    public void onConfigChanged(ConfigData configData);
}
