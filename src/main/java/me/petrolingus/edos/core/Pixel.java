package me.petrolingus.edos.core;

public class Pixel {

    double x;

    double y;

    int ttl = 23;

    int ttlStart = ttl;

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

    public double getTtlPercent() {
        return ((double) ttl / (double) ttlStart);
    }

    public void show() {
        ttl--;
    }

    public boolean isDead() {
        return ttl < 0;
    }
}
