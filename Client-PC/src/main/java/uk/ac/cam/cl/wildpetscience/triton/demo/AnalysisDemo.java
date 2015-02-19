package uk.ac.cam.cl.wildpetscience.triton.demo;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.ConfigData;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Driver;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.OutputSink;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.PassthroughFilter;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis.Analysis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.function.Function;

public class AnalysisDemo {
    private final JFrame frame;
    private final Driver driver;

    public AnalysisDemo(Function<OutputSink<PassthroughFilter.Passthrough<ImageWithCorners, AnimalPosition>>,
            Driver<PassthroughFilter.Passthrough<ImageWithCorners, AnimalPosition>>> creator,
                        ConfigData configData,
                        Analysis analysis,
                        String title) {
        frame = new JFrame(title);
        frame.setSize(640, 480);

        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        AnalysisTestPanel panel = new AnalysisTestPanel(configData, analysis);
        frame.getContentPane().add(panel);

        driver = creator.apply(panel);

        frame.addWindowListener(new WindowListener() {
            @Override public void windowOpened(WindowEvent windowEvent) { }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                driver.cancel();
            }

            @Override public void windowClosed(WindowEvent windowEvent) {  }
            @Override public void windowIconified(WindowEvent windowEvent) {  }
            @Override public void windowDeiconified(WindowEvent windowEvent) {  }
            @Override public void windowActivated(WindowEvent windowEvent) {  }
            @Override public void windowDeactivated(WindowEvent windowEvent) {  }
        });
    }

    public void start() {
        EventQueue.invokeLater(() -> frame.setVisible(true));

        driver.start();
    }
}
