package me.petrolingus.edos.core.electrods;

import me.petrolingus.edos.core.math.Constants;

public class VerticalElectrode {

    public enum Mode {
        CONSTANT,
        WAVE
    }

    public static boolean isActive;

    public static Mode mode;

    public static double significand;
    public static double exponent;

    public static double amplitude;
    public static double frequency;
    public static double phase;

    public static double getAcceleration() {
        switch (mode) {
            case CONSTANT -> {
                return significand * Math.pow(10, exponent);
            }
            case WAVE -> {
                return amplitude * Math.sin(Constants.PI2 * frequency * System.currentTimeMillis() / 1000.0 + phase);
            }
        }
        return 0;
    }

}
