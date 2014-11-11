package com.stewsters.shipwright;


import com.stewsters.shipwright.internals.GridMap;
import com.stewsters.shipwright.internals.TileType;
import com.stewsters.shipwright.noise.OpenSimplexNoise;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ShipWrightInterior {

    public final static Color transparent = new Color(0, 0, 0, 0);

    public static BufferedImage generate(Blueprint blueprint) {

        Random r = new Random();
        OpenSimplexNoise openSimplexNoise = new OpenSimplexNoise();

        // This seems to lose a lot of detail if you get too far away from the center.
        BufferedImage output = new BufferedImage(blueprint.width, blueprint.height, BufferedImage.TYPE_INT_ARGB);
        GridMap gridMap = new GridMap(blueprint.width, blueprint.height);

        //Generate rooms
        for (int i = 0; i < 10; i++) {

            int x = r.nextInt(gridMap.map.length / 2);
            int y = r.nextInt(gridMap.map[0].length);

            int roomX = r.nextInt(5) + 3;
            int roomY = r.nextInt(5) + 3;

            if (gridMap.testRoom(x, y, x + roomX, y + roomY, TileType.AETHER)) {
                gridMap.writeRoom(x, y, x + roomX, y + roomY, TileType.FLOOR);
            }
        }


        int xColorOffset = r.nextInt(200) - 100;
        int yColorOffset = r.nextInt(200) - 100;

        int xStructureOffset = r.nextInt(200) - 100;
        int yStructureOffset = r.nextInt(200) - 100;

        for (int x = 0; x < output.getWidth() / 2; x++) {
            for (int y = 0; y < output.getHeight(); y++) {

                Color color;

                if (!gridMap.map[x][y].equals(TileType.AETHER) && !gridMap.map[x][y].equals(TileType.VACUUM)) {
                    color = gridMap.map[x][y].color;
                } else {
                    int specRGB = blueprint.spec.getRGB(
                        (int) (((float) x / (float) output.getWidth()) * blueprint.spec.getWidth()),
                        (int) (((float) y / (float) output.getHeight()) * blueprint.spec.getHeight()));

                    boolean showHere = specRGB == Color.BLACK.getRGB();

                    showHere |= specRGB == Color.WHITE.getRGB() &&
                        (openSimplexNoise.eval((x + xStructureOffset) * (10f / blueprint.width), (y + yStructureOffset) * (10f / blueprint.height)) > 0);

                    if (showHere) {

                        int index = (int) (blueprint.colorPalette.colors.size() * (openSimplexNoise.eval((x + xColorOffset) * (10f / blueprint.width), (y + yColorOffset) * (10f / blueprint.height)) + 1) / 2.0);
                        color = blueprint.colorPalette.colors.get(index);

                    } else {
                        color = transparent;
                    }

                }

                output.setRGB(x, y, color.getRGB());
                output.setRGB(output.getWidth() - 1 - x, y, color.getRGB());

            }
        }


        return output;

    }

}
