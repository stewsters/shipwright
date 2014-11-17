package com.stewsters.shipwright.internals;


import com.stewsters.util.mapgen.CellType;

import java.awt.*;

public enum TileType implements CellType{

    AETHER("Aether", Color.GREEN, 1.0f, true), // This is used as unknown
    VACUUM("Vacuum", new Color(0, 0, 0, 0), 1.0f, true),
    FLOOR("Floor", Color.CYAN, 0.05f, false),
    WALL("Wall", Color.BLUE, 4.0f, true),
    REINFORCED_WALL("Reinforced Wall", Color.blue.darker(), 100.0f, true), // There should not be a path through here unless required
    INTERNALS("Internals", Color.darkGray, 3.0f, true),
    ARMOR("Armor", Color.gray, 1.0f, true),
    DOOR("Door", Color.GREEN.darker(), 0.1f, true);

    public String name;
    public Color color;
    public float digTunnelCost; // Cost of running a hallway through this tile
    public boolean blocks; // Prevents movement

    TileType(String name, Color color, float digTunnelCost, boolean blocks) {

        this.name = name;
        this.color = color;
        this.digTunnelCost = digTunnelCost;
        this.blocks = blocks;

    }




}
