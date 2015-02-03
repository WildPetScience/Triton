package uk.ac.cam.cl.wildpetscience.triton.demo;

import uk.ac.cam.cl.wildpetscience.triton.lib.models.Box;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zone;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zones;
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
        zoneSet.add(new Zone(new Box(0.9, 0.1, 0.2, 0.2), "FOOD"));
        Zones zones = new Zones(zoneSet, 100, 200);
        PositionTestPanel panel = new PositionTestPanel(zones,
                new AnalysisOutputSink());
        frame.getContentPane().add(panel);
    }

    public void start() {
        EventQueue.invokeLater(() -> frame.setVisible(true));
    }
}
