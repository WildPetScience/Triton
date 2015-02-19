package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline.tracker;

import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.util.LinkedList;
import java.util.Queue;

import static org.opencv.imgproc.Imgproc.*;

/**
 * Algorithm that, given an image depicting areas of motion
 * outputs the center of motion of the largest cluster
 *
 * Created by Vlad on 19/02/2015.
 */

//
public class ClusteringModule {
    private double prob;
    private boolean[][] used, movement;

    private int area, maxArea, totalArea;
    private int rows, cols;
    private final int vicinity = 1;

    private int xCurr, yCurr, xAns, yAns;

    private Mat image;

    public ClusteringModule(Mat img) {
        totalArea = 0;
        prob = 0.0;
        rows = img.rows();
        cols = img.cols();

        xAns = 0;
        yAns = 0;
        image = img;

        if(rows == 0 || cols == 0)
            return;

        used = new boolean[rows][cols];
        movement = new boolean[rows][cols];

        dilate(img, img, getStructuringElement(MORPH_RECT, new Size(50, 50)));
        erode(img, img, getStructuringElement(MORPH_RECT, new Size(20, 20)));

        for(int i = 0; i < rows; ++i)
            for(int j = 0; j < cols; ++j) {
                used[i][j] = false;
                if (img.get(i, j)[0] == 255)
                    movement[i][j] = true;
                else
                    movement[i][j] = false;
            }

        for(int i = 0; i < rows; ++i)
            for(int j = 0; j < cols; ++j)
                if(used[i][j] == false && movement[i][j] == true) {
                    area = 0;
                    xCurr = 0;
                    yCurr = 0;

                    bfs(i, j);

                    if(area > maxArea) {
                        xAns = xCurr;
                        yAns = yCurr;
                        maxArea = area;
                    }

                    totalArea += area;
                }

    }

    private class IntPoint {
        public int x, y;
        public IntPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private void bfs(int y, int x) {
        Queue<IntPoint> elements = new LinkedList<IntPoint>();

        elements.add(new IntPoint(x, y));

        int xMin=x, xMax=x, yMin=y, yMax=y;

        used[y][x] = true;

        while(!elements.isEmpty()) {
            IntPoint e = elements.remove();
            x = e.x;
            y = e.y;

            xCurr += x;
            yCurr += y;

            ++area;

            if(x > xMax)
                xMax = x;
            if(y > yMax)
                yMax = y;
            if(x < xMin)
                xMin = x;
            if(y < yMin)
                yMin = y;

            for(int newY = y - vicinity; newY <= y + vicinity; ++newY) {
                for (int newX = x - vicinity; newX <= x + vicinity; ++newX) {
                    if (newX < 0 || newY < 0 || newX >= cols || newY >= rows)
                        continue;
                    if (movement[newY][newX] == false || used[newY][newX] == true)
                        continue;

                    used[newY][newX] = true;
                    elements.add(new IntPoint(newX, newY));
                }
            }
        }

        if(area == 0)
            return;

        xCurr = xCurr / area;
        yCurr = yCurr / area;

        xCurr = (2*xCurr + xMin + xMax) / 4;
        yCurr = (2*yCurr + yMin + yMax) / 4;
    }

    double getX() {
        if(maxArea == 0 || cols == 0)
            return 0.0;
        return 1.0 * xAns / cols;
    }

    double getY() {
        if(maxArea == 0 || cols == 0)
            return 0.0;
        return 1.0 * yAns / rows;
    }

    double getConfidence() {
        if(maxArea == 0 || cols == 0)
            return 0.0;
        double prob = 1.0 * maxArea / totalArea;

        System.out.println(maxArea + " " + totalArea);
        if(maxArea < 1500)
            prob = prob / 4;
        if(maxArea < 2000)
            prob = prob / 2;
        return prob;
    }
}