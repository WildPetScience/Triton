package uk.ac.cam.cl.wildpetscience.triton.demo;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.ac.cam.cl.wildpetscience.triton.lib.Bootstrap;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Box;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.ConfigData;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zone;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Driver;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.InputFailedException;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.PassthroughFilter;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis.AnalysisOutputSink;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.processing.CornerDetectionFilter;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.processing.NoiseReductionFilter;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.tracker.TrackingFilter;
import uk.ac.cam.cl.wildpetscience.triton.pipeline.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Driver.makeSimpleDriver;

/**
 * Demo classes that help development of the pipeline
 */
public class Demos extends JFrame {
    public static void main(String[] args) throws ParseException {
        Bootstrap.init();

        Options opts = new Options();
        opts.addOption("d", "dir", true, "Path to the TestImageData repo");
        opts.addOption("w", "webcam", true, "Webcam number to use (default 0)");

        CommandLine cmd = new GnuParser().parse(opts, args);
        int webcam = Integer.valueOf(cmd.getOptionValue('w', "0"));

        String path = cmd.getOptionValue('d', "../TestImageData");

        Demos demos = new Demos(new File(path), webcam);
        demos.setVisible(true);
    }

    private final JList<TestVideoEnumerator.TestVideo> videoList;
    private final TestVideoEnumerator enumerator;

    public Demos(File testDirectory, final int webcam) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel choose = new JPanel();
        choose.setLayout(new BoxLayout(choose, BoxLayout.LINE_AXIS));

        enumerator = new TestVideoEnumerator(testDirectory, webcam);

        videoList = new JList<>(enumerator.getSourceArray());

        videoList.setSelectedIndex(0);

        choose.add(new JLabel("Choose an input:"));
        choose.add(videoList);

        JPanel grid = new JPanel();
        GridLayout gridLayout = new GridLayout(0, 2);
        gridLayout.setHgap(3);
        gridLayout.setVgap(3);
        grid.setLayout(gridLayout);

        add(choose);
        add(grid);

        grid.add(new JLabel("Input video:"));
        JButton inputVideo = new JButton("Start");
        inputVideo.addActionListener(e -> {
            VisualPipelineDemo demo = new VisualPipelineDemo(
                    output -> makeSimpleDriver(
                            getInputSource(),
                            output
                    ), "Input video");
            demo.start();
        });
        grid.add(inputVideo);

        grid.add(new JLabel("Noise reduction demo:"));
        JButton noiseReduction = new JButton("Start");
        noiseReduction.addActionListener(e -> {
            VisualPipelineDemo demo = new VisualPipelineDemo(
                    output -> new Driver<>(
                            getInputSource(),
                            new NoiseReductionFilter(),
                            output
                    ), "Noise reduction");
            demo.start();
        });
        grid.add(noiseReduction);

        grid.add(new JLabel("Corner detection demo:"));
        JButton cornerDetection = new JButton("Start");
        cornerDetection.addActionListener(e -> {
            VisualPipelineDemo demo = new VisualPipelineDemo(
                    output -> new Driver<>(
                            getInputSource(),
                            new CornerDetectionFilter(),
                            new CornerDisplayFilter(),
                            output
                    ), "Corner detection");
            demo.start();
        });
        grid.add(cornerDetection);

        grid.add(new JLabel("Location tracking demo:"));
        JButton locationTracking = new JButton("Start");
        locationTracking.addActionListener(e -> {
            VisualPipelineDemo demo = new VisualPipelineDemo(
                    output -> new Driver<>(
                            getInputSource(),
                            new DummyCornerDetectionFilter(),
                            new PassthroughFilter<>(new TrackingFilter()),
                            new TrackingDisplayFilter(),
                            output
                    ), "Location tracking");
            demo.start();
        });
        grid.add(locationTracking);

        grid.add(new JLabel("Motion diff demo:"));
        JButton motionDiff = new JButton("Start");
        motionDiff.addActionListener(e -> {
            VisualPipelineDemo demo = new VisualPipelineDemo(
                    output -> new Driver<>(
                            getInputSource(),
                            new DummyCornerDetectionFilter(),
                            new MotionDiffFilter(new TrackingFilter()),
                            output
                    ), "Motion diff");
            demo.start();
        });
        grid.add(motionDiff);

        Set<Zone> zoneSet = new HashSet<>();
        zoneSet.add(new Zone(new Box(0.2, 0.2, 0.2, 0.2), "WATER"));
        zoneSet.add(new Zone(new Box(0.8, 0.6, 0.2, 0.2), "FOOD"));
        ConfigData config = new ConfigData(zoneSet, 100, 200, "http://localhost:8080/condor");
        grid.add(new JLabel("Complete demo:"));
        JButton completeDemo = new JButton("Start");
        completeDemo.addActionListener(e -> {
            AnalysisDemo demo = new AnalysisDemo(
                    output -> new Driver<>(
                            getInputSource(),
                            new CornerDetectionFilter(),
                            new PassthroughFilter<>(new TrackingFilter()),
                            output),
                    config,
                    new AnalysisOutputSink(config),
                    "Complete demo");
            demo.start();
        });
        grid.add(completeDemo);

        grid.add(new JLabel("Position classification demo:"));
        JButton positionClassification = new JButton("Start");
        positionClassification.addActionListener(e -> {
            LocationPipelineDemo demo = new LocationPipelineDemo(
                    "Position classification");
            demo.start();
        });
        grid.add(positionClassification);

        pack();
    }

    public ImageInputSource getInputSource() {
        TestVideoEnumerator.TestVideo vid = videoList.getSelectedValue();
        try {
            return enumerator.getInputSource(vid, 16);
        } catch (InputFailedException e) {
            throw new RuntimeException(e);
        }
    }
}
