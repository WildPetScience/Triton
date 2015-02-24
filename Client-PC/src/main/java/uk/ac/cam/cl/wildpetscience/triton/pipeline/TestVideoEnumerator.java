package uk.ac.cam.cl.wildpetscience.triton.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.InputFailedException;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.WebcamInputSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds all test footage available on the current device and creates
 * ImageInputSource for different streams.
 */
public class TestVideoEnumerator {

    private ArrayList<TestVideo> sources = new ArrayList<>();

    private static ArrayList<EnclosedTestVideo> additionalSources = new ArrayList<>();

    private final int webcam;

    public TestVideoEnumerator(File path, int webcam) {
        this.webcam = webcam;
        enumerateChangeDetection(path);
    }

    private void enumerateChangeDetection(File root) {
        sources.add(new WebcamVideo(webcam));

        sources.addAll(additionalSources);

        File changeDetectionRoot = new File(root, "changedetection");
        if (!changeDetectionRoot.isDirectory()) {
            System.err.println("Changedetection.net dataset not present");
            return;
        }

        for (String video : changeDetectionRoot.list()) {
            TestChangeDetectionVideo result = new TestChangeDetectionVideo();

            File dir = new File(changeDetectionRoot, video);
            result.input = new File(dir, "input");
            result.groundTruth = new File(dir, "groundtruth");
            result.name = video;
            if (!result.input.isDirectory() || !result.groundTruth.isDirectory()) {
                System.err.printf(
                        "Incomplete dataset %s found in changedetection\n",
                        video);
                continue;
            }
            sources.add(result);
        }
    }

    /**
     * Gets all available test video sources.
     * @return
     */
    public List<TestVideo> getSources() {
        return sources;
    }

    public TestVideo[] getSourceArray() {
        return sources.toArray(new TestVideo[sources.size()]);
    }

    public ImageInputSource getInputSource(TestVideo testVideo, int delay) throws InputFailedException {
        if (testVideo instanceof TestChangeDetectionVideo) {
            File photoDir = ((TestChangeDetectionVideo)testVideo).input;
            return new ImageSequenceInputSource(photoDir, "in%06d.jpg", delay);
        } else if (testVideo instanceof WebcamVideo) {
            return new WebcamInputSource(((WebcamVideo)testVideo).webcam);
        } else if (testVideo instanceof EnclosedTestVideo) {
            return ((EnclosedTestVideo)testVideo).create();
        }
        return null;
    }

    public static class TestVideo {

    }
    public static class TestChangeDetectionVideo extends TestVideo {
        File input, groundTruth;
        String name;

        @Override
        public String toString() {
            return name;
        }
    }

    public static class WebcamVideo extends TestVideo {
        public final int webcam;

        public WebcamVideo(int webcam) {
            this.webcam = webcam;
        }

        @Override
        public String toString() {
            return "Live webcam (" + webcam + ")";
        }
    }

    public static abstract class EnclosedTestVideo extends TestVideo {
        public abstract ImageInputSource create();
    }

    public static void addAdditionalSource(EnclosedTestVideo source) {
        additionalSources.add(source);
    }
}
