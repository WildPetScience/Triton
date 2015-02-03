package uk.ac.cam.cl.wildpetscience.triton.demo;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.ac.cam.cl.wildpetscience.triton.lib.Bootstrap;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.Driver;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.InputFailedException;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.processing.NoiseReductionFilter;
import uk.ac.cam.cl.wildpetscience.triton.pipeline.TestVideoEnumerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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

        grid.add(new JLabel("Noise reduction demo:"));
        JButton noiseReduction = new JButton("Start");
        noiseReduction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VisualPipelineDemo demo = new VisualPipelineDemo(
                        output -> new Driver<>(
                                getInputSource(),
                                new NoiseReductionFilter(),
                                output
                        ), "Noise reduction");
                demo.start();
            }
        });
        grid.add(noiseReduction);

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
