package com.stewsters.shipwright;


import java.awt.Color;
import java.util.List;
import java.util.Random;

public class ColorPalette {

    public List<Color> colors;


    public ColorPalette() {
        colors = ColorGenerator.generate(20, 2);
    }

}
