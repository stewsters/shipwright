package com.stewsters.shipwright;

import com.stewsters.shipwright.color.ColorPalette;

import java.awt.image.BufferedImage;

public class Blueprint {

    public boolean symmetrical;
    public BufferedImage spec;

    public ColorPalette colorPalette;

    // All ships are assumed to go face upwards
    public int width; //in pixels
    public int height;


}
