package uk.ac.cam.cl.wildpetscience.triton.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.demo.VisualPipelineDemo;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.ImageInputSource;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * A JFrame that displays a list of test sources for running processing on.
 */
public class TestVideoChooser extends JFrame {
    private final TestVideoEnumerator enumerator;

    private final JList<TestVideoEnumerator.TestVideo> jList;

    public TestVideoChooser(File testDirectory) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        enumerator = new TestVideoEnumerator(testDirectory);
        jList = new JList<>(enumerator.getSourceArray());

        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                super.mouseClicked(evt);
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    TestVideoEnumerator.TestVideo video =
                            jList.getSelectedValue();
                    startVideo(video);
                }
            }
        });

        setTitle("Choose a test source");

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JLabel("Please choose an input"));
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(jList);
        pack();
    }

    private void startVideo(TestVideoEnumerator.TestVideo video) {
        ImageInputSource source = enumerator.getInputSource(video, 16);
        VisualPipelineDemo demo = new VisualPipelineDemo(source, video.toString());
        demo.start();
    }
}
