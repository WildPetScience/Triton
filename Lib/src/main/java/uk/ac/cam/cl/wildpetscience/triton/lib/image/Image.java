package uk.ac.cam.cl.wildpetscience.triton.lib.image;

import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.time.LocalDateTime;

/**
 * An image
 */
public class Image {
    /**
     * The image data encoded in an OpenCV Mat
     */
    private Mat data;

    /**
     * The time at which this image was captured.
     */
    private LocalDateTime timestamp;

    public Image() {
        data = new Mat();
        timestamp = LocalDateTime.now();
    }

    public Image(Mat data) {
        this.data = data;
        timestamp = LocalDateTime.now();
    }

    public Image(Mat data, LocalDateTime timestamp) {
        this.data = data;
        this.timestamp = timestamp;
    }

    /**
     * Copy constructor - copies the Mat inside the Image
     * @param img
     */
    public Image(Image img) {
        this(img.getData().clone(), img.timestamp);
    }

    public Mat getData() {
        return data;
    }

    public void release() {
        if (data != null) {
            data.release();
            data = null;
        }
    }

    public BufferedImage toAwtImage() {
        int type = 0;
        Mat mat = getData();
        if (mat.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (mat.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        } else {
            return null;
        }

        if (mat.width() <= 0 || mat.height() <= 0) {
            return null;
        }

        BufferedImage image = new BufferedImage(mat.width(), mat.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        mat.get(0, 0, data);

        return image;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
