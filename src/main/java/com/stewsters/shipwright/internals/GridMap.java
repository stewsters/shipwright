package com.stewsters.shipwright.internals;


import com.stewsters.util.pathing.twoDimention.shared.Mover2d;
import com.stewsters.util.pathing.twoDimention.shared.PathNode2d;
import com.stewsters.util.pathing.twoDimention.shared.TileBasedMap2d;

public class GridMap implements TileBasedMap2d {

    private TileType[][] map;
    private boolean[][] partOfShip;
    private int width;
    private int height;


    public GridMap(int xSize, int ySize) {

        width = xSize;
        height = ySize;

        map = new TileType[width][height];
        partOfShip = new boolean[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = TileType.AETHER;
                partOfShip[x][y] = false;
            }
        }


    }


    public void writeToBothSides(int x, int y, TileType tileType) {

        map[x][y] = tileType;
        map[width - 1 - x][y] = tileType;

    }

    public boolean testBothSides(int x, int y, TileType tileType) {

        return map[x][y] == tileType &&
            map[width - 1 - x][y] == tileType;
    }

    public void writeRoom(int xLow, int yLow, int xHigh, int yHigh, TileType floorType, TileType wallType) {

        for (int x = xLow; x <= xHigh; x++) {
            for (int y = yLow; y <= yHigh; y++) {
                if (x == xLow || y == yLow || x == xHigh || y == yHigh) {
                    writeToBothSides(x, y, wallType);
                } else {
                    writeToBothSides(x, y, floorType);
                }
            }
        }
    }

    public boolean testRoom(int xLow, int yLow, int xHigh, int yHigh, TileType tileType) {

        if (xLow < 0 || yLow < 0 || xHigh >= width / 2 || yHigh >= height)
            return false;

        for (int x = xLow; x <= xHigh; x++) {
            for (int y = yLow; y <= yHigh; y++) {
//                if (x < 0 || x >= map.length / 2)
//                    return false;

                if (!testBothSides(x, y, tileType)) {
                    return false;
                }

            }
        }
        return true;
    }


    @Override
    public int getWidthInTiles() {
        return width;
    }

    @Override
    public int getHeightInTiles() {
        return height;
    }


    public TileType getTile(int x, int y) {
        return map[x][y];
    }


    public void trimNonContiguous(int xStart, int yStart) {

        partOfShip[xStart][yStart] = true;

        // expand
        int added = 1;

        while (added > 0) {
            added = 0;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (map[x][y] == TileType.INTERNALS && partOfShip[x][y] == false) {

                        if (isPartOfShip(x + 1, y) ||
                            isPartOfShip(x - 1, y) ||
                            isPartOfShip(x, y + 1) ||
                            isPartOfShip(x, y - 1)) {
                            partOfShip[x][y] = true;
                            added++;
                        }
                    }

                }
            }

        }


        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!partOfShip[x][y])
                    map[x][y] = TileType.AETHER;
            }
        }

    }


    // Tests a location to see if its already part
    private boolean isPartOfShip(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return false;
        return partOfShip[x][y];

    }


    @Override
    public void pathFinderVisited(int x, int y) {

    }

    @Override
    public boolean isBlocked(Mover2d mover, PathNode2d pathNode) {
        return false;
    }

    @Override
    public boolean isBlocked(Mover2d mover, int x1, int y1) {
        return false;
    }

    @Override
    public float getCost(Mover2d mover, int sx, int sy, int tx, int ty) {

        return 0;
    }
}
