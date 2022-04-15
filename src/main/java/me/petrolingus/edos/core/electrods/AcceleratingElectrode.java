package me.petrolingus.edos.core.electrods;

public class AcceleratingElectrode {

    public static boolean isActive = false;

    public static double significand;
    public static double exponent;

    public static double getAcceleration() {
        return significand * Math.pow(10, exponent);
    }

}
