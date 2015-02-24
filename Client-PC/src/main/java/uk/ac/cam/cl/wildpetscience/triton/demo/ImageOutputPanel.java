package uk.ac.cam.cl.wildpetscience.triton.demo;

import org.opencv.core.Mat;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.OutputSink;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;

/**
 * A JPanel that displays outputted images
 */
public class ImageOutputPanel extends JPanel implements OutputSink<Image> {

    private BufferedImage image;

    @Override
    public void onDataAvailable(Image image) {
        if (image == null) {
            return;
        }
        if (image.getData() == null) System.out.println   ("sfsfsfsdsf");
        this.image = image.toAwtImage();
        image.release();
        EventQueue.invokeLater(() -> repaint());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }

    @Override
    public void close() throws IOException {
    }
}
