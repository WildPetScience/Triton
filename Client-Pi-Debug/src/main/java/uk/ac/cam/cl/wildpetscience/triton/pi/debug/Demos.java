package uk.ac.cam.cl.wildpetscience.triton.pi.debug;

import org.apache.commons.cli.ParseException;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;
import uk.ac.cam.cl.wildpetscience.triton.pipeline.TestVideoEnumerator;

/**
 * Runs the demos on the Pi.
 */
public class Demos {
    public static void main(String[] args) throws ParseException {
        TestVideoEnumerator.addAdditionalSource(new TestVideoEnumerator.EnclosedTestVideo() {
            @Override
            public ImageInputSource create() {
                return null;
            }

            @Override
            public String toString() {
                return "Raspberry Pi Camera";
            }
        });
        uk.ac.cam.cl.wildpetscience.triton.demo.Demos.main(args);
    }
}
