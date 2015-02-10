package uk.ac.cam.cl.wildpetscience.triton.demo;

import org.opencv.core.Point;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.*;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Box;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis.Analysis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.time.LocalDateTime;
import java.util.List;
import java.util.LinkedList;

/**
 * Lets the user input location data with the mouse.
 */
public class PositionTestPanel extends JPanel implements MouseMotionListener, MouseWheelListener, MouseListener {

    private final ConfigData config;
    private final Analysis analysis;
    private double probability = 0.5;

    private int x, y;

    public PositionTestPanel(ConfigData config, Analysis analysis) {
        this.config = config;
        this.analysis = analysis;
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawString(String.format("Probability: %.2f", probability), 10, 15);
        g2.draw(new Ellipse2D.Double(x-2, y-2, 4, 4)); // -2 for centering

        for (Zone zone : config.getZones()) {
            Box scaled = zone.area.scale(getWidth(), getHeight());
            Rectangle2D.Double rect = new Rectangle2D.Double(
                    (int) scaled.getLeft(),
                    (int) scaled.getTop(),
                    (int) scaled.getWidth(),
                    (int) scaled.getHeight()
            );
            g2.setColor(new Color(100,100,100,100));
            g2.fill(rect);
        }

        /* Draw the path */
        List<DataFrame> path = analysis.getPath();
        Point lastPoint = null;
        for (DataFrame data : path) {
            Point location = data.getLocation();
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
            if (!data.getZoneId().equals("N/A")) g2.setColor(Color.BLUE); // point BLUE if in user-defined zone
            g2.draw(plotted);
            g2.fill(plotted);
            lastPoint = location;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        probability += (double)e.getWheelRotation() / 100.0;
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        onNewEvent();
    }

    /* Unused mouse events */
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

    private void onNewEvent() {
        AnimalPosition position = new AnimalPosition(
                new Point(
                        (double)x / (double)getWidth(),
                        (double)y / (double)getHeight()),
                LocalDateTime.now(), probability);
        analysis.onDataAvailable(position);
    }
}
