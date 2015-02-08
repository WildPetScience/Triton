package uk.ac.cam.cl.wildpetscience.triton.pi.debug;

import org.apache.commons.cli.ParseException;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;
import uk.ac.cam.cl.wildpetscience.triton.pi.camera.CameraOpts;
import uk.ac.cam.cl.wildpetscience.triton.pi.camera.PiCameraInputSource;
import uk.ac.cam.cl.wildpetscience.triton.pipeline.TestVideoEnumerator;

/**
 * Runs the demos on the Pi.
 */
public class Demos {
    public static void main(String[] args) throws ParseException {
        TestVideoEnumerator.addAdditionalSource(new TestVideoEnumerator.EnclosedTestVideo() {
            @Override
            public ImageInputSource create() {
                return new PiCameraInputSource(new CameraOpts(640,480,16, true));
            }

            @Override
            public String toString() {
                return "Raspberry Pi Camera";
            }
        });
        uk.ac.cam.cl.wildpetscience.triton.demo.Demos.main(args);
    }
}
