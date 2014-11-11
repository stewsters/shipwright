package com.stewsters.shipwright.noise;


import com.stewsters.shipwright.noise.OpenSimplexNoise;

public class NoiseGenerator {
    OpenSimplexNoise openSimplexNoise;

    public NoiseGenerator() {
        openSimplexNoise = new OpenSimplexNoise();
    }

    public double get(float x, float y) {

        return (1 + Math.sin((x + openSimplexNoise.eval(x , y ) / 2.0) * 50.0)) / 2.0;

    }


}
