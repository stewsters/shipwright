package com.stewsters.shipwright;


import com.stewsters.shipwright.noise.NoiseGenerator;
import com.stewsters.shipwright.noise.OpenSimplexNoise;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ShipWright {

    public final static Color transparent = new Color(0, 0, 0, 0);

    public static BufferedImage generate(Blueprint blueprint) {

        BufferedImage output = new BufferedImage(blueprint.width, blueprint.height, BufferedImage.TYPE_INT_ARGB);


        Random r = new Random();
        int xColorOffset = r.nextInt(200) - 100;
        int yColorOffset = r.nextInt(200) - 100;


        int xStructureOffset = r.nextInt(200) - 100;
        int yStructureOffset = r.nextInt(200) - 100;

        // This seems to lose a lot of detail if you get too far away from the center.
        OpenSimplexNoise openSimplexNoise = new OpenSimplexNoise();


        for (int x = 0; x < output.getWidth() / 2; x++) {
            for (int y = 0; y < output.getHeight(); y++) {

                int specRGB = blueprint.spec.getRGB(
                        (int) (((float) x / (float) output.getWidth()) * blueprint.spec.getWidth()),
                        (int) (((float) y / (float) output.getHeight()) * blueprint.spec.getHeight()));

                boolean showHere = specRGB == Color.BLACK.getRGB();

                showHere |= specRGB == Color.WHITE.getRGB() &&
                        (openSimplexNoise.eval((x + xStructureOffset) * (10f / blueprint.width), (y + yStructureOffset) * (10f / blueprint.height)) > 0);

                Color color;
                if (showHere) {

                    int index = (int) (blueprint.colorPalette.colors.size() * (openSimplexNoise.eval((x + xColorOffset) * (10f / blueprint.width), (y + yColorOffset)  * (10f / blueprint.height)) + 1) / 2.0);
                    color = blueprint.colorPalette.colors.get(index);

                } else {
                    color = transparent;
                }
                output.setRGB(x, y, color.getRGB());
                output.setRGB(output.getWidth() - 1 - x, y, color.getRGB());

            }
        }


        return output;

    }

}
