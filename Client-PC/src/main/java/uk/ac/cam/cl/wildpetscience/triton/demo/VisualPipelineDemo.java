package uk.ac.cam.cl.wildpetscience.triton.demo;

import nu.pattern.OpenCV;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Driver;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class VisualPipelineDemo {
    private final JFrame frame;
    private final Driver driver;

    public VisualPipelineDemo(ImageInputSource input, String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);

        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        ImageOutputPanel panel = new ImageOutputPanel();
        frame.getContentPane().add(panel);

        driver = Driver.makeSimpleDriver(input, panel);

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
