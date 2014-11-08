package com.stewsters.shipwright;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * From StackOverflow
 *
 * http://stackoverflow.com/questions/43044/algorithm-to-randomly-generate-an-aesthetically-pleasing-color-palette
 */

public class ColorGenerator {

    public static List<Color> generate(int colors, int skip) {
        final int lastindex = colors + skip;
        return new Points(colors + skip).asList().subList(skip, lastindex);
    }

    // RYB color space
    private static class RYB {
        private static final double[] White = { 1, 1, 1 };
        private static final double[] Red = { 1, 0, 0 };
        private static final double[] Yellow = { 1, 1, 0 };
        private static final double[] Blue = { 0.163, 0.373, 0.6 };
        private static final double[] Violet = { 0.5, 0, 0.5 };
        private static final double[] Green = { 0, 0.66, 0.2 };
        private static final double[] Orange = { 1, 0.5, 0 };
        private static final double[] Black = { 0.2, 0.094, 0.0 };

        public static double[] ToRgb(double r, double y, double b) {
            double[] rgb = new double[3];
            for (int i = 0; i < 3; i++) {
                rgb[i] = White[i] * (1.0 - r) * (1.0 - b) * (1.0 - y) + Red[i] * r * (1.0 - b) * (1.0 - y)
                        + Blue[i] * (1.0 - r) * b * (1.0 - y) + Violet[i] * r * b * (1.0 - y) + Yellow[i]
                        * (1.0 - r) * (1.0 - b) * y + Orange[i] * r * (1.0 - b) * y + Green[i] * (1.0 - r) * b
                        * y + Black[i] * r * b * y;
            }

            return rgb;
        }
    }

    private static class Points {
        private final int pointsCount;
        private double[] picked;
        private int pickedCount;

        private final List<double[]> points = new ArrayList<double[]>();

        private Points(int count) {
            pointsCount = count;
        }

        private Points generate() {
            points.clear();
            int numBase = (int) Math.ceil(Math.pow(pointsCount, 1.0 / 3.0));
            int ceil = (int) Math.pow(numBase, 3.0);
            for (int i = 0; i < ceil; i++) {
                points.add(new double[] { Math.floor(i / (double) (numBase * numBase)) / (numBase - 1.0),
                        Math.floor((i / (double) numBase) % numBase) / (numBase - 1.0),
                        Math.floor(i % numBase) / (numBase - 1.0), });
            }

            return this;
        }

        private double distance(double[] p1) {
            double distance = 0;
            for (int i = 0; i < 3; i++) {
                distance += Math.pow(p1[i] - picked[i], 2.0);
            }

            return distance;
        }

        private double[] pick() {
            if (picked == null) {
                picked = points.remove(0);
                pickedCount = 1;
                return picked;
            }

            double d1 = distance(points.get(0));
            int i1 = 0, i2 = 0;
            for (double[] point : points) {
                double d2 = distance(point);
                if (d1 < d2) {
                    i1 = i2;
                    d1 = d2;
                }

                i2 += 1;
            }

            double[] pick = points.remove(i1);

            for (int i = 0; i < 3; i++) {
                picked[i] = (pickedCount * picked[i] + pick[i]) / (pickedCount + 1.0);
            }

            pickedCount += 1;
            return pick;
        }

        public List<Color> asList() {
            generate();
            List<Color> colors = new ArrayList<>();
            for (int i = 0; i < pointsCount; i++) {
                final double[] point = pick();
                double[] rgb = RYB.ToRgb(point[0], point[1], point[2]);

                Color color = new Color(getPastelColorPercentage(rgb[0]), getPastelColorPercentage(rgb[1]), getPastelColorPercentage(rgb[2]));
                colors.add(color);
            }

            return colors;
        }

        private int getColorPercentage(final double channelPercentage) {
            return (int) Math.floor(255 * channelPercentage);
        }

        private int getPastelColorPercentage(final double channelPercentage) {
            return (int) Math.round(127 * channelPercentage + Math.random() * 100);
        }
    }
}