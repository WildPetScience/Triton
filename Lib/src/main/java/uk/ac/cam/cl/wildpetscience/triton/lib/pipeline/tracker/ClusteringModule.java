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
    private static double prob;
    private static boolean[][] used, movement;

    private static int area, maxArea, totalArea;
    private static int rows, cols;
    private static final int vicinity = 1;

    private static int xCurr, yCurr, xAns, yAns;

    private static Mat image;

    private ClusteringModule(Mat img) {
    }

    public static void Process(Mat img) {
        totalArea = 0;
        prob = 0.0;

        if (rows != img.rows() || cols != img.cols()) {
            rows = img.rows();
            cols = img.cols();

            used = new boolean[rows][cols];
            movement = new boolean[rows][cols];
        }

        xAns = 0;
        yAns = 0;
        maxArea = 0;
        prob = 0;
        image = img;

        if (rows == 0 || cols == 0)
            return;

        dilate(img, img, getStructuringElement(MORPH_RECT, new Size(20, 20)));

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

    private static class IntPoint {
        public int x, y;
        public IntPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static void bfs(int y, int x) {
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

    public static double getX() {
        if(maxArea == 0 || cols == 0)
            return 0.0;
        return 1.0 * xAns / cols;
    }

    public static double getY() {
        if(maxArea == 0 || cols == 0)
            return 0.0;
        return 1.0 * yAns / rows;
    }

    public static double getConfidence() {
        if(maxArea == 0 || cols == 0)
            return 0.0;
        double prob = 1.0 * maxArea / totalArea;

        if(maxArea < 1500)
            prob = prob / 4;
        if(maxArea < 2000)
            prob = prob / 2;
        return prob;
    }
}