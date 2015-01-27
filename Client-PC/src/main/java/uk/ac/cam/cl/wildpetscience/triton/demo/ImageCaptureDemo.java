package uk.ac.cam.cl.wildpetscience.triton.demo;

import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.BufferedInputSource;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Driver;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.InputFailedException;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.WebcamInputSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;

public class ImageCaptureDemo {
    public static void main(String[] args) throws IOException {
        OpenCV.loadShared();
        final JFrame frame = new JFrame("Image capture demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);

        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        ImageOutputPanel panel = new ImageOutputPanel();
        frame.getContentPane().add(panel);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });

        final Driver driver = new Driver(new BufferedInputSource(new WebcamInputSource(0)), panel);

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
        driver.start();
    }
}
