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

        int blackX = -1;
        int blackY = -1;

        // Generate underlying structure;
        int xStructureOffset = r.nextInt(200) - 100;
        int yStructureOffset = r.nextInt(200) - 100;
        for (int x = 0; x < gridMap.getWidth() / 2; x++) {
            for (int y = 0; y < gridMap.getHeight(); y++) {

                int specRGB = blueprint.spec.getRGB(
                    (int) (((float) x / (float) output.getWidth()) * blueprint.spec.getWidth()),
                    (int) (((float) y / (float) output.getHeight()) * blueprint.spec.getHeight()));

                boolean showHere = specRGB == Color.BLACK.getRGB();
                if (showHere) {
                    blackX = x;
                    blackY = y;
                }


                showHere |= specRGB == Color.WHITE.getRGB() &&
                    (openSimplexNoise.eval((x + xStructureOffset) * (10f / blueprint.width), (y + yStructureOffset) * (10f / blueprint.height)) > 0);

                if (showHere)
                    gridMap.writeToBothSides(x, y, TileType.INTERNALS);
            }
        }

        //Trim off the underlying structure that is not contiguous
        gridMap.trimNonContiguous(blackX, blackY);

        //Generate rooms in underlying structure
        for (int i = 0; i < 100; i++) {

            //We start with large rooms (11-20) then work our way down to smaller closets
            int roomX = r.nextInt(10) + ((100 - i) / 10) + 2;
            int roomY = r.nextInt(10) + ((100 - i) / 10) + 2; // actual room size will be 2 smaller, as we need walls

            int x = r.nextInt(gridMap.getWidth() / 2 - roomX);
            int y = r.nextInt(gridMap.getHeight() - roomY);


            boolean primed = false; // we have seen a valid spot for a room.
            boolean placed = false; // we have placed the room

            // we need to move in until we are over valid terrain
            // move x in
            while (!placed) {
                x++;
                boolean valid = gridMap.testRoom(x - 1, y - 1, x + roomX + 1, y + roomY + 1, TileType.INTERNALS);
                if (valid) {
                    primed = true;
                } else if (primed) {
                    //then we are no longer valid.  rewind.
                    placed = true;
                    x--;
                }

                if (x >= gridMap.getWidth())
                    break;
            }

            primed = false;
            placed = false;
            // move Y in
            while (!placed) {
                y++;

                boolean valid = gridMap.testRoom(x - 1, y - 1, x + roomX + 1, y + roomY + 1, TileType.INTERNALS);
                if (valid) {
                    primed = true;
                } else if (primed) {
                    //then we are no longer valid.  rewind.
                    placed = true;
                    y--;
                }

                if (y >= gridMap.getHeight())
                    break;
            }

            if (gridMap.testRoom(x, y, x + roomX, y + roomY, TileType.INTERNALS)) {
                gridMap.writeRoom(x, y, x + roomX, y + roomY, TileType.FLOOR, TileType.WALL);
            }
        }


        int xColorOffset = r.nextInt(200) - 100;
        int yColorOffset = r.nextInt(200) - 100;


        //Print

        for (int x = 0; x < output.getWidth() / 2; x++) {
            for (int y = 0; y < output.getHeight(); y++) {

                Color color;

                if (gridMap.getTile(x, y) == TileType.INTERNALS) {

                    int index = (int) (blueprint.colorPalette.colors.size() * (openSimplexNoise.eval((x + xColorOffset) * (10f / blueprint.width), (y + yColorOffset) * (10f / blueprint.height)) + 1) / 2.0);
                    color = blueprint.colorPalette.colors.get(index);

                } else if (!gridMap.getTile(x, y).equals(TileType.AETHER) && !gridMap.getTile(x, y).equals(TileType.VACUUM)) {
                    color = gridMap.getTile(x, y).color;
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
