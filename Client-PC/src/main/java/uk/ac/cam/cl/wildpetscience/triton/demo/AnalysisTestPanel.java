package uk.ac.cam.cl.wildpetscience.triton.demo;

import org.opencv.core.Point;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.ImageWithCorners;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.*;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Box;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.OutputSink;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.PassthroughFilter;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis.Analysis;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.List;

/**
 * Tests the analysis system with real data being passed in.
 */
public class AnalysisTestPanel extends JPanel implements OutputSink<PassthroughFilter.Passthrough<ImageWithCorners, AnimalPosition>> {

    private final ConfigData config;
    private final Analysis analysis;
    private double probability = 0.5;

    private ImageWithCorners image;

    public AnalysisTestPanel(ConfigData config, Analysis analysis) {
        this.config = config;
        this.analysis = analysis;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        if (image != null) {
            g.drawImage(image.toAwtImage(), 0, 0, getWidth(), getHeight(), null);
        }

        Corners corners = image == null ? new Corners() : image.getCorners();

        g2.drawString(String.format("Probability: %.2f", probability), 10, 15);

        for (Zone zone : config.getZones()) {
            Box scaled = zone.area.scale(getWidth(), getHeight());
            Rectangle2D.Double rect = new Rectangle2D.Double(
                    (int) scaled.getLeft(),
                    (int) scaled.getTop(),
                    (int) scaled.getWidth(),
                    (int) scaled.getHeight()
            );
            Point tl = corners.getInverseTransform(zone.area.getTopLeft());
            Point tr = corners.getInverseTransform(zone.area.getTopRight());
            Point bl = corners.getInverseTransform(zone.area.getBottomLeft());
            Point br = corners.getInverseTransform(zone.area.getBottomRight());
            Polygon shape = new Polygon(
                    new int[] {
                            (int) (tl.x * getWidth()),
                            (int) (tr.x * getWidth()),
                            (int) (br.x * getWidth()),
                            (int) (bl.x * getWidth())
                    }, new int[] {
                            (int) (tl.y * getHeight()),
                            (int) (tr.y * getHeight()),
                            (int) (br.y * getHeight()),
                            (int) (bl.y * getHeight())
            }, 4);
            g2.setColor(new Color(100,100,100,100));
            g2.fill(shape);
        }

        /* Draw the path */
        List<PositionDataFrame> path = analysis.getPath();
        Point lastPoint = null;
        for (PositionDataFrame data : path) {
            Point location = corners.getInverseTransform(data.getLocation());
            int xNew = (int) (location.x * getWidth());
            int yNew = (int) (location.y * getHeight());
            /* Draw lines */
            if (lastPoint != null) {
                int xOld = (int) (lastPoint.x * getWidth());
                int yOld = (int) (lastPoint.y * getHeight());
                g2.setColor(Color.RED);
                g2.drawLine(xOld, yOld, xNew, yNew);
            }
            /* Plot points */
            Ellipse2D.Double plotted = new Ellipse2D.Double(xNew-2, yNew-2, 4, 4); // -2 for centering
            if (data.getZone() != null) g2.setColor(Color.BLUE); // point BLUE if in user-defined zone
            g2.draw(plotted);
            g2.fill(plotted);
            lastPoint = location;
        }
    }

    @Override
    public void onDataAvailable(PassthroughFilter.Passthrough<ImageWithCorners, AnimalPosition> data) {
        analysis.onDataAvailable(data.data);
        probability = data.data.getProbability();
        image = data.passthrough;
        EventQueue.invokeLater(() -> repaint());
    }

    @Override
    public void close() throws IOException {
        analysis.close();
    }
}
