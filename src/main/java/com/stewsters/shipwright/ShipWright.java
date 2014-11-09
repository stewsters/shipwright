package com.stewsters.shipwright;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ShipWright {

    public final static Color transparent = new Color(0, 0, 0, 0);

    public static BufferedImage generate(Blueprint blueprint) {

        BufferedImage output = new BufferedImage(blueprint.width, blueprint.height, BufferedImage.TYPE_INT_ARGB);

        // This seems to lose a lot of detail if you get too far away from the center.
        OpenSimplexNoise openSimplexNoise = new OpenSimplexNoise();

        Random r = new Random();
        int xColorOffset = r.nextInt(200) - 100;
        int yColorOffset = r.nextInt(200) - 100;


        int xStructureOffset = r.nextInt(200) - 100;
        int yStructureOffset = r.nextInt(200) - 100;

        for (int x = 0; x < output.getWidth() / 2; x++) {
            for (int y = 0; y < output.getHeight(); y++) {


                boolean showHere = blueprint.spec.getRGB(x, y) == Color.BLACK.getRGB();


                showHere |= blueprint.spec.getRGB(x, y) == Color.WHITE.getRGB() &&
                        (openSimplexNoise.eval((x + xStructureOffset) / 10f, (y + yStructureOffset) / 10f) > 0);

                Color color;
                if (showHere) {

                    int index = (int) (blueprint.colorPalette.colors.size() * (openSimplexNoise.eval((x + xColorOffset) / 10f, (y + yColorOffset) / 10f) + 1) / 2.0);

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
