package uk.ac.cam.cl.wildpetscience.triton.demo;

import org.opencv.core.Rect;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zone;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zones;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Driver;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.OutputSink;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis.AnalysisOutputSink;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class LocationPipelineDemo {
    private final JFrame frame;

    public LocationPipelineDemo(String title) {
        frame = new JFrame(title);
        frame.setSize(640, 480);

        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        Set<Zone> zoneSet = new HashSet<>();
        Zones zones = new Zones(zoneSet);
        PositionTestPanel panel = new PositionTestPanel(zones,
                new AnalysisOutputSink());
        frame.getContentPane().add(panel);
    }

    public void start() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }
}
