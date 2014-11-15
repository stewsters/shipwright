package com.stewsters.shipwright;

import com.stewsters.shipwright.color.ColorPalette;

import java.awt.image.BufferedImage;

/**
 * The blueprint represents the prototype features that we want to see in our final ship
 */
public class Blueprint {

    // A black, white, and transparent image used to lay out the ship.
    public BufferedImage spec;

    // The colors we will use on the outside of the ship
    public ColorPalette colorPalette;

    // All ships are assumed to go face upwards
    public int width; //in pixels
    public int height;


}
