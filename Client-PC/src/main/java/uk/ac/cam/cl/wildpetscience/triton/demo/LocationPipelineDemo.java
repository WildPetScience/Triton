package uk.ac.cam.cl.wildpetscience.triton.demo;


import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigManager;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Box;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.ConfigData;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zone;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis.AnalysisOutputSink;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class LocationPipelineDemo {
    private final JFrame frame;

    public LocationPipelineDemo(String title) {
        frame = new JFrame(title);
        frame.setSize(640, 480);

        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        Set<Zone> zoneSet = new HashSet<>();
        zoneSet.add(new Zone(new Box(0.2, 0.2, 0.2, 0.2), "WATER"));
        zoneSet.add(new Zone(new Box(0.8, 0.6, 0.2, 0.2), "FOOD"));
        ConfigData config = new ConfigData(zoneSet, 100, 200, "http://localhost:8080/condor", "Hamster", null);
        PositionTestPanel panel = new PositionTestPanel(config,
                new AnalysisOutputSink(config));
        frame.getContentPane().add(panel);
    }

    public void start() {
        EventQueue.invokeLater(() -> frame.setVisible(true));
    }
}
