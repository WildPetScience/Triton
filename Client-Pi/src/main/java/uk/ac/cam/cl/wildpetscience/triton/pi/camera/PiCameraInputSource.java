package uk.ac.cam.cl.wildpetscience.triton.pi.camera;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.InputFailedException;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

/**
 * An ImageInputSource that fetches images from a Raspberry Pi camera.
 */
public class PiCameraInputSource implements ImageInputSource {

    private RaspiStillRunner runner;
    private long imageCount = -1;
    private long lastSentImage = -1;
    private BufferedImage currentImage;

    private Object notifier = new Object();

    private CameraOpts opts;

    public PiCameraInputSource(CameraOpts opts) {
        this.opts = opts;
    }

    @Override
    public Image getNext() throws InputFailedException {
        if (runner == null) {
            try {
                runner = new RaspiStillRunner(opts,
                        img -> imageAvailable(img));
            } catch (IOException e) {
                // RPi camera failed to open
                e.printStackTrace();
            }
        }

        long localImageCount;
        long localLastSentImage;
        synchronized (this) {
            localImageCount = imageCount;
            localLastSentImage = lastSentImage;
        }
        while (localImageCount == localLastSentImage) {
            synchronized (notifier) {
                try {
                    notifier.wait();
                } catch (InterruptedException e) {
                }
            }
            synchronized (this) {
                localImageCount = imageCount;
                localLastSentImage = lastSentImage;
            }
        }

        BufferedImage image;
        synchronized (this) {
            image = currentImage;
            lastSentImage = imageCount;
        }

        if (image == null) {
            System.out.println();
        }

        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat result = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        result.put(0, 0, pixels);
        return new Image(result);
    }

    private synchronized void imageAvailable(BufferedImage image) {
        currentImage = image;
        imageCount++;
        synchronized (notifier) {
            notifier.notifyAll();
        }
    }

    @Override
    public void close() throws IOException {
        if (runner != null) {
            runner.close();
        }
    }
}
