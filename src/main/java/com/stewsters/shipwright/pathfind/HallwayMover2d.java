package com.stewsters.shipwright.pathfind;

import com.stewsters.shipwright.internals.GridMap;
import com.stewsters.util.pathing.twoDimention.shared.Mover2d;
import com.stewsters.util.pathing.twoDimention.shared.PathNode2d;

public class HallwayMover2d implements Mover2d {

    GridMap gridMap;

    public HallwayMover2d(GridMap gameMap) {
        this.gridMap = gameMap;
    }


    @Override
    public boolean canTraverse(PathNode2d pathNode) {
//        gridMap.get getCellTypeAt(pathNode.x, pathNode.y);
        return true;
    }


}
