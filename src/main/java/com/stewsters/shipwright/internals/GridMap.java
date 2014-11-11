package com.stewsters.shipwright.internals;


public class GridMap {

    public TileType[][] map;


    public GridMap(int xSize, int ySize) {
        map = new TileType[xSize][ySize];


        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                map[x][y] = TileType.AETHER;
            }
        }


    }


    public void writeToBothSides(int x, int y, TileType tileType) {

        map[x][y] = tileType;
        map[map.length - 1 - x][y] = tileType;

    }

    public boolean testBothSides(int x, int y, TileType tileType) {

        return map[x][y] == tileType && map[map.length - 1 - x][y] == tileType;
    }

    public void writeRoom(int xLow, int yLow, int xHigh, int yHigh, TileType tileType) {

        for (int x = xLow; x <= xHigh; x++) {
            for (int y = yLow; y <= yHigh; y++) {
                writeToBothSides(x, y, tileType);
            }
        }
    }

    public boolean testRoom(int xLow, int yLow, int xHigh, int yHigh, TileType tileType) {

        if (xLow < 0 || yLow < 0 || xHigh >= map.length / 2 || yHigh >= map[0].length)
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

}
