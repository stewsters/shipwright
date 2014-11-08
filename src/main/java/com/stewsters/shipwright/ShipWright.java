package com.stewsters.shipwright;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ShipWright {


    public static BufferedImage generate(Blueprint blueprint) {

        BufferedImage output = new BufferedImage(blueprint.width, blueprint.height, BufferedImage.TYPE_INT_ARGB);

        // This seems to lose a lot of detail if you get too far away from the center.
        OpenSimplexNoise openSimplexNoise = new OpenSimplexNoise();

        Random r = new Random();
        int xOffset = r.nextInt(200) - 100;
        int yOffset = r.nextInt(200) - 100;

        for (int x = 0; x < output.getWidth() / 2; x++) {
            for (int y = 0; y < output.getHeight(); y++) {

                int index = (int) (blueprint.colorPalette.colors.size() * (openSimplexNoise.eval((x + xOffset) / 10f, (y + yOffset) / 10f) + 1) / 2.0);

                Color color = blueprint.colorPalette.colors.get(index);

                output.setRGB(x, y, color.getRGB());
                output.setRGB(output.getWidth() - 1 - x, y, color.getRGB());
            }
        }


        return output;

    }


}
