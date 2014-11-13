package com.stewsters.shipwright.internals;


public class GridMap {

    private TileType[][] map;
    private int width;
    private int height;


    public GridMap(int xSize, int ySize) {

        width = xSize;
        height = ySize;

        map = new TileType[width][height];


        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = TileType.AETHER;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TileType getTile(int x, int y) {
        return map[x][y];
    }
}
