package com.stewsters.shipwright;


import com.stewsters.shipwright.internals.GridMap;
import com.stewsters.shipwright.internals.TileType;
import com.stewsters.shipwright.noise.OpenSimplexNoise;
import com.stewsters.shipwright.pathfind.HallwayMover2d;
import com.stewsters.shipwright.pathfind.HallwayPathFinder;
import com.stewsters.util.math.Point2i;
import com.stewsters.util.pathing.twoDimention.shared.FullPath2d;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
        generateRooms(blueprint, spacecraft);
        generateHallways(blueprint, spacecraft);
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
        for (int x = 0; x < spacecraft.gridMap.getWidthInTiles() / 2; x++) {
            for (int y = 0; y < spacecraft.gridMap.getHeightInTiles(); y++) {

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

    private static Spacecraft generateRooms(Blueprint blueprint, Spacecraft spacecraft) {

        spacecraft.rooms = new ArrayList<>();

        //Generate rooms in underlying structure
        for (int i = 0; i < 100; i++) {

            //We start with large rooms (11-20) then work our way down to smaller closets
            int roomX = r.nextInt(10) + ((100 - i) / 10) + 2;
            int roomY = r.nextInt(10) + ((100 - i) / 10) + 2; // actual room size will be 2 smaller, as we need walls

            int x = r.nextInt(spacecraft.gridMap.getWidthInTiles() / 2 - roomX);
            int y = r.nextInt(spacecraft.gridMap.getHeightInTiles() - roomY);


            boolean primed = false; // we have seen a valid spot for a room.
            boolean placed = false; // we have placed the room

            // we need to move in until we are over valid terrain
            // move x in
            while (!placed) {
                x++;
                boolean valid = spacecraft.gridMap.testRoom(x - 1, y - 1, x + roomX + 1, y + roomY + 1, TileType.INTERNALS);
                if (valid) {
                    primed = true;
                } else if (primed) {
                    //then we are no longer valid.  rewind.
                    placed = true;
                    x--;
                }

                if (x >= spacecraft.gridMap.getWidthInTiles())
                    break;
            }

            primed = false;
            placed = false;
            // move Y in
            while (!placed) {
                y++;

                boolean valid = spacecraft.gridMap.testRoom(x - 1, y - 1, x + roomX + 1, y + roomY + 1, TileType.INTERNALS);
                if (valid) {
                    primed = true;
                } else if (primed) {
                    //then we are no longer valid.  rewind.
                    placed = true;
                    y--;
                }

                if (y >= spacecraft.gridMap.getHeightInTiles())
                    break;
            }

            if (spacecraft.gridMap.testRoom(x, y, x + roomX, y + roomY, TileType.INTERNALS)) {
                spacecraft.rooms.add(new Room(x, y, x + roomX, y + roomY));
                spacecraft.gridMap.writeRoom(x, y, x + roomX, y + roomY, TileType.FLOOR, TileType.WALL);
            }
        }
        return spacecraft;
    }

    private static Spacecraft generateHallways(Blueprint blueprint, Spacecraft spacecraft) {

        // Cutting paths
        HallwayPathFinder pathFinder2d = new HallwayPathFinder(spacecraft.gridMap, spacecraft.gridMap.getWidthInTiles() * spacecraft.gridMap.getHeightInTiles(), false);
        HallwayMover2d hallwayMover2d = new HallwayMover2d(spacecraft.gridMap);

        // for each pair of room centers
        for (Room roomFrom : spacecraft.rooms) {
            for (Room roomTo : spacecraft.rooms) {

                Point2i roomCenterFrom = roomFrom.center();
                Point2i roomCenterTo = roomTo.center();

                FullPath2d fullPath2d = pathFinder2d.findPath(hallwayMover2d, roomCenterFrom.x, roomCenterFrom.y, roomCenterTo.x, roomCenterTo.y);

                if (fullPath2d != null) {
                    for (int step = 0; step < fullPath2d.getLength(); step++) {
                        TileType tileType = spacecraft.gridMap.getTile(fullPath2d.getX(step), fullPath2d.getY(step));

                        if (tileType != TileType.DOOR) {
                            spacecraft.gridMap.writeToBothSides(fullPath2d.getX(step), fullPath2d.getY(step), TileType.FLOOR);
                        }

                    }
                }
            }
        }

        return spacecraft;
    }

    private static Spacecraft paintShip(Blueprint blueprint, Spacecraft spacecraft) {
        int xColorOffset = r.nextInt(200) - 100;
        int yColorOffset = r.nextInt(200) - 100;

        spacecraft.output = new BufferedImage(blueprint.width, blueprint.height, BufferedImage.TYPE_INT_ARGB);
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
