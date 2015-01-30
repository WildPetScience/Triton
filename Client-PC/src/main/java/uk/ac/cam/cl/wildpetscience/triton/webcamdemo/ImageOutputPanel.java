package uk.ac.cam.cl.wildpetscience.triton.webcamdemo;

import org.opencv.core.Mat;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageOutputSink;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;

/**
 * A JPanel that displays outputted images
 */
public class ImageOutputPanel extends JPanel implements ImageOutputSink {
    static BufferedImage createAwtImage(Mat mat) {
        int type = 0;
        if (mat.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (mat.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        } else {
            return null;
        }

        BufferedImage image = new BufferedImage(mat.width(), mat.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        mat.get(0, 0, data);

        return image;
    }

    private BufferedImage image;

    @Override
    public void onImageAvailable(Image image) {
        this.image = createAwtImage(image.getData());
        image.release();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                repaint();
            }
        });
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
