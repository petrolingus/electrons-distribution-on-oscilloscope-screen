package me.petrolingus.edos.core;

public class Pixel {

    double x;

    double y;

    double brightness = 1;

    public Pixel(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getBrightness() {
        return brightness;
    }

    public void decrementBrightness() {
        brightness -= 0.25;
    }

    public boolean isDead() {
        return brightness < 0;
    }
}
