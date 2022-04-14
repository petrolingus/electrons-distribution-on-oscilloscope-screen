package me.petrolingus.edos.core;

public class Particle {

    double x;
    double y;

    double z;

    double vx;

    double vy;

    double vz;

    public int ttl = 100_000_000;

    private static final double R = 0.1;
    private static final double THETA = 0.3;
    private static final double PHI = 360;

    public static double verticalB;
    public static double horizontalB = 0.8;

    private double dt = 0.01;

    public Particle() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        double thetaInDegrees = (2 * Math.random() - 1) * THETA;
        double phiInDegrees = (2 * Math.random() - 1) * PHI;
        double theta = Math.toRadians(thetaInDegrees);
        double phi = Math.toRadians(phiInDegrees);
        this.vx = R * Math.sin(theta) * Math.cos(phi);
        this.vy = R * Math.sin(theta) * Math.sin(phi);
        this.vz = R * Math.cos(theta);
    }

    public void move() {
        x += vx * dt;
        y += vy * dt;
        z += vz * dt;

        if (z > 0.3 && z < 0.4) {
            vx -= horizontalB * vz * dt;
            vz += horizontalB * vx * dt;
        }

        if (z > 0.3 && z < 0.4) {
            vy += verticalB * vz * dt;
            vz -= verticalB * vy * dt;
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
