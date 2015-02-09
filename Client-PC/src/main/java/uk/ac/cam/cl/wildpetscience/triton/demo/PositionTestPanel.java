package uk.ac.cam.cl.wildpetscience.triton.demo;

import org.opencv.core.Point;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.*;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Box;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis.Analysis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.time.LocalDateTime;

/**
 * Lets the user input location data with the mouse.
 */
public class PositionTestPanel extends JPanel implements MouseMotionListener, MouseWheelListener {

    private final Zones zones;
    private final Analysis analysis;
    private double probability = 0.5;

    private int x, y;

    public PositionTestPanel(Zones zones, Analysis analysis) {
        this.zones = zones;
        this.analysis = analysis;
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawString(String.format("Probability: %.2f", probability), 10, 15);
        g2.draw(new Ellipse2D.Double(x, y, 4, 4));

        for (Zone zone : zones.getZones()) {
            Box scaled = zone.area.scale(getWidth(), getHeight());
            g2.drawRect(
                    (int)scaled.getX(),
                    (int)scaled.getY(),
                    (int)scaled.getWidth(),
                    (int)scaled.getHeight());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        EventQueue.invokeLater(() -> repaint());
        onNewEvent();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        probability += (double)e.getWheelRotation() / 100.0;
        EventQueue.invokeLater(() -> repaint());
        onNewEvent();
    }

    private void onNewEvent() {
        AnimalPosition position = new AnimalPosition(
                new Point(
                        (double)x / (double)getWidth(),
                        (double)y / (double)getHeight()),
                LocalDateTime.now(), probability);
        analysis.onDataAvailable(position);
    }
}
