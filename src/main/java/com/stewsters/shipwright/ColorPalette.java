package com.stewsters.shipwright;


import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ColorPalette {

    public List<Color> colors;


    public ColorPalette() {

    }

    public void generate() {
        colors = ColorGenerator.generate(40, 2);
    }


    public ColorPalette sub() {

        Random random = new Random();
        ColorPalette output = new ColorPalette();

        output.colors = new ArrayList<>();

        for (int i = 0; i < random.nextInt(2) + 1; i++) {

            Color color = colors.get(random.nextInt(colors.size()));

            output.colors.add(color.darker());
            output.colors.add(color);
            output.colors.add(color.brighter());

            if (random.nextBoolean())
                Collections.shuffle(colors);

        }

        return output;

    }

}
