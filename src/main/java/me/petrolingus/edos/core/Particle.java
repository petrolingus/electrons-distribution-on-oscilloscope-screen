package me.petrolingus.edos.core;

import me.petrolingus.edos.core.math.Constants;

public class Particle {

    double x;
    double y;

    double z;

    double vx;

    double vy;

    double vz;

    public int ttl = 100_000_000;

    private static final double R = 1.3e7;
    private static final double THETA = 25;

    public static double verticalE = 0;
    public static double horizontalE = 0;

    private double dt = 10e-8;

    public Particle() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        double thetaInDegrees = (2 * Math.random() - 1) * THETA;
        double theta = Math.toRadians(thetaInDegrees);
        double phi = Constants.PI2 * Math.random();
        this.vx = R * Math.sin(theta) * Math.cos(phi);
        this.vy = R * Math.sin(theta) * Math.sin(phi);
        this.vz = R * Math.cos(theta);
    }

    public void move() {
        x += vx * dt;
        y += vy * dt;
        z += vz * dt;

        if (z > 0.3 && z < 0.4) {
            vx += horizontalE * dt;
        }

        if (z > 0.3 && z < 0.4) {
            vy += verticalE * dt;
        }

        ttl--;
    }

    public boolean isDead() {
        return ttl < 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
