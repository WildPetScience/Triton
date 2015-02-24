package uk.ac.cam.cl.wildpetscience.triton;

import org.opencv.core.Point;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Box;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Plots a wild animal in a panel.
 */
public class WildAnimalPlotPanel extends JPanel implements MouseMotionListener, MouseListener {

    List<Zone> zones = new ArrayList<>();
    List<AnimalPosition> path;

    private JTextField zoneName;

    public WildAnimalPlotPanel(List<AnimalPosition> path, JTextField zoneName) {
        this.path = path;
        this.zoneName = zoneName;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        for (Zone zone : zones) {
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
        Point lastPoint = null;
        for (AnimalPosition data : path) {
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
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    private int sx, sy, fx, fy;

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        sx = e.getX();
        sy = e.getY();
    }
    public void mouseReleased(MouseEvent e) {
        fx = e.getX();
        fy = e.getY();

        double cx = (double)(sx + fx) / 2.0 / (double)getWidth();
        double cy = (double)(sy + fy) / 2.0 / (double)getHeight();

        double w = (double)(fx - sx) / (double)getWidth();
        double h = (double)(fy - sy) / (double)getWidth();

        zones.add(new Zone(cx, cy, w, h, zoneName.getText()));
        repaint();
    }
}
