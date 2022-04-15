package me.petrolingus.edos.core;

public class HorizontalPane {

    public static double significand = 0.5;
    public static double exponent = 0;

    public static double getAcceleration() {
        return significand * Math.pow(10, exponent);
    }

}
