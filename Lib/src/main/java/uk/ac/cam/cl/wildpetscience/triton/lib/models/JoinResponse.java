package uk.ac.cam.cl.wildpetscience.triton.lib.models;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by brucecollie on 03/03/15.
 */
public class JoinResponse {

    public Map<String, String> dbo;
    public Date dateConnected;
    public Double cageHeight;
    public String identifier;
    public Integer animalTypeId;
    public List<Zone> zones;
    public Double cageWidth;
    public List<AnimalPosition> positions;
    public Map<String, String> animalType;

}
