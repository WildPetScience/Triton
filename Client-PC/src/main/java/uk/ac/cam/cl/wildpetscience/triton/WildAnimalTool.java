package uk.ac.cam.cl.wildpetscience.triton;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.opencv.core.Point;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigManager;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.AnimalPosition;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.ConfigData;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.PositionDataFrame;
import uk.ac.cam.cl.wildpetscience.triton.lib.models.Zone;
import uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.analysis.AnalysisOutputSink;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Puts the "Wild" in Wild Pet Science.
 */
public class WildAnimalTool extends JFrame {
    WildAnimalPlotPanel panel;

    public WildAnimalTool(File csv) throws IOException {
        setSize(800, 600);
        setTitle("Wild animal tool");
        CSVParser parser = CSVParser.parse(csv, Charset.defaultCharset(), CSVFormat.DEFAULT.withHeader());
        double minLon = 360, maxLon = -360;
        double minLat = 360, maxLat = -360;

        List<AnimalPosition> path = new ArrayList<>();

        for (CSVRecord record : parser) {
            String timestamp = record.get("timestamp");
            String sLon = record.get("location-long");
            String sLat = record.get("location-lat");
            if (timestamp == null || sLon == null || sLat == null) {
                System.err.println("Invalid CSV line found");
                continue;
            }
            LocalDateTime time = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
            double lon = Double.parseDouble(sLon);
            double lat = Double.parseDouble(sLat);

            if (lon < minLon) {
                minLon = lon;
            }
            if (lon > maxLon) {
                maxLon = lon;
            }
            if (lat < minLat) {
                minLat = lat;
            }
            if (lat > maxLat) {
                maxLat = lat;
            }

            path.add(new AnimalPosition(new Point(lon, lat), time, 1.0));
        }

        for (AnimalPosition pos : path) {
            pos.getLocation().x = (pos.getLocation().x - minLon) / (maxLon - minLon);
            pos.getLocation().y = (pos.getLocation().y - minLat) / (maxLat - minLat);
        }

        BoxLayout layout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        getContentPane().setLayout(layout);

        JTextField animalName = new JTextField();
        animalName.setMaximumSize(new Dimension(300, (int) animalName.getPreferredSize().getHeight()));

        JTextField zoneName = new JTextField();
        zoneName.setMaximumSize(new Dimension(200, (int)zoneName.getPreferredSize().getHeight()));

        panel = new WildAnimalPlotPanel(path, zoneName);
        add(animalName);
        add(zoneName);
        add(panel);

        final double width = maxLon - minLon, height = maxLat - minLat;

        JButton upload = new JButton("Upload the wild animal to the INTERNET");
        upload.addActionListener(e -> {
            ConfigData configData = new ConfigData(
                    new HashSet<>(panel.zones),
                    width,
                    height,
                    ConfigManager.getRemoteServer(),
                    animalName.getText());
            AnalysisOutputSink output = new AnalysisOutputSink(configData);
            for (AnimalPosition pos : panel.path) {
                output.onDataAvailable(pos);
            }
            try {
                output.close();
            } catch (IOException e1) {
            }
        });
        add(upload);
    }

    public void start() {
        EventQueue.invokeLater(() -> setVisible(true));
    }
}
