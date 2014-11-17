package com.stewsters.shipwright.pathfind;

import com.stewsters.shipwright.internals.GridMap;
import com.stewsters.shipwright.internals.TileType;
import com.stewsters.util.pathing.twoDimention.pathfinder.AStarPathFinder2d;
import com.stewsters.util.pathing.twoDimention.shared.Mover2d;

public class HallwayPathFinder extends AStarPathFinder2d {

    GridMap gridMap;

    public HallwayPathFinder(GridMap map, int maxSearchDistance, boolean allowDiagMovement) {
        super(map, maxSearchDistance, allowDiagMovement);
        this.gridMap = map;
    }

    @Override
    protected boolean isBlocked(Mover2d mover, int x, int y) {
        // We want to be able to tunnel through ways to create hallways

        if (gridMap.getTile(x, y) == TileType.AETHER || gridMap.getTile(x, y) == TileType.VACUUM)
            return true;
        else
            return false;
    }

    @Override
    public float getMovementCost(Mover2d mover, int sx, int sy, int tx, int ty) {
        TileType tileType = gridMap.getTile(tx, ty);
        return tileType.digTunnelCost;
    }
}
