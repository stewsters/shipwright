package com.stewsters.shipwright;


import com.stewsters.shipwright.internals.GridMap;
import com.stewsters.shipwright.internals.TileType;
import com.stewsters.shipwright.noise.OpenSimplexNoise;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ShipWright {

    public final static Color transparent = new Color(0, 0, 0, 0);
    private final static Random r = new Random();
    private final static OpenSimplexNoise openSimplexNoise = new OpenSimplexNoise();


    /**
     * Generates a ship
     *
     * @param blueprint A Blueprint that contains parameters for ship creation
     * @return A Buffered Image of the ship
     */
    public static Spacecraft generate(Blueprint blueprint) {

        Spacecraft spacecraft = new Spacecraft();
        spacecraft.gridMap = new GridMap(blueprint.width, blueprint.height);

        generateUnderlyingStructure(blueprint, spacecraft);

        paintShip(blueprint, spacecraft);

        return spacecraft;

    }

    /**
     * Generates the ship's shape
     *
     * @param blueprint
     * @param spacecraft
     */
    private static void generateUnderlyingStructure(Blueprint blueprint, Spacecraft spacecraft) {

        int blackX = -1;
        int blackY = -1;

        // Generate underlying structure;
        int xStructureOffset = r.nextInt(200) - 100;
        int yStructureOffset = r.nextInt(200) - 100;
        for (int x = 0; x < spacecraft.gridMap.getWidth() / 2; x++) {
            for (int y = 0; y < spacecraft.gridMap.getHeight(); y++) {

                int specRGB = blueprint.spec.getRGB(
                    (int) (((float) x / (float) blueprint.width) * blueprint.spec.getWidth()),
                    (int) (((float) y / (float) blueprint.height) * blueprint.spec.getHeight()));

                boolean showHere = specRGB == Color.BLACK.getRGB();
                if (showHere) {
                    blackX = x;
                    blackY = y;
                }


                showHere |= specRGB == Color.WHITE.getRGB() &&
                    (openSimplexNoise.eval((x + xStructureOffset) * (10f / blueprint.width), (y + yStructureOffset) * (10f / blueprint.height)) > 0);

                if (showHere)
                    spacecraft.gridMap.writeToBothSides(x, y, TileType.INTERNALS);
            }
        }

        //Trim off the underlying structure that is not contiguous
        spacecraft.gridMap.trimNonContiguous(blackX, blackY);
    }

    private static Spacecraft paintShip(Blueprint blueprint, Spacecraft spacecraft) {
        int xColorOffset = r.nextInt(200) - 100;
        int yColorOffset = r.nextInt(200) - 100;

       spacecraft.output =  new BufferedImage(blueprint.width, blueprint.height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < spacecraft.output.getWidth() / 2; x++) {
            for (int y = 0; y < spacecraft.output.getHeight(); y++) {

                Color color;

                if (spacecraft.gridMap.getTile(x, y) == TileType.INTERNALS) {

                    int index = (int) (blueprint.colorPalette.colors.size() * (openSimplexNoise.eval((x + xColorOffset) * (10f / blueprint.width), (y + yColorOffset) * (10f / blueprint.height)) + 1) / 2.0);
                    color = blueprint.colorPalette.colors.get(index);

                } else if (!spacecraft.gridMap.getTile(x, y).equals(TileType.AETHER) && !spacecraft.gridMap.getTile(x, y).equals(TileType.VACUUM)) {
                    color = spacecraft.gridMap.getTile(x, y).color;
                } else {

                    color = transparent;

                }

                spacecraft.output.setRGB(x, y, color.getRGB());
                spacecraft.output.setRGB(spacecraft.output.getWidth() - 1 - x, y, color.getRGB());

            }
        }
        return spacecraft;
    }


}
