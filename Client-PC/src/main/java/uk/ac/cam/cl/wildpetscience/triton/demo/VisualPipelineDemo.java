package uk.ac.cam.cl.wildpetscience.triton.demo;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Driver;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.OutputSink;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.function.Function;

public class VisualPipelineDemo {
    private final JFrame frame;
    private final Driver driver;

    public VisualPipelineDemo(ImageInputSource input, String title) {
        this(panel -> Driver.makeSimpleDriver(input, panel), title);
    }

    public VisualPipelineDemo(Function<OutputSink<Image>,
                                      Driver<Image>> creator,
                              String title) {
        frame = new JFrame(title);
        frame.setSize(640, 480);

        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        ImageOutputPanel panel = new ImageOutputPanel();
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
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });

        driver.start();
    }
}
