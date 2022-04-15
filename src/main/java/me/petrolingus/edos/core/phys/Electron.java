package me.petrolingus.edos.core.phys;

import me.petrolingus.edos.core.electrods.AcceleratingElectrode;
import me.petrolingus.edos.core.electrods.FocusingElectrode;
import me.petrolingus.edos.core.electrods.HorizontalElectrode;
import me.petrolingus.edos.core.electrods.VerticalElectrode;

public class Electron {

    private double x;

    private double y;

    private double z;

    private double vx;

    private double vy;

    private double vz;

    private int ttl = 100_000;

    private boolean isAlive = true;

    public static double time;

    public Electron(double vx, double vy, double vz) {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public boolean cantMove() {
        boolean electronOnScreen = onScreen();
        boolean ttlLessZero = ttl < 0;
        return electronOnScreen || ttlLessZero;
    }

    public boolean onScreen() {
        return z > 1;
    }

    public void move() {
        double dz = vz * time;
        if (z + dz < 1) {
            x += vx * time;
            y += vy * time;
        } else {
            double distToScreen = 1.0 - z;
            double newTime = distToScreen / vz;
            x += vx * newTime;
            y += vy * newTime;
        }
        z += dz;

        if (FocusingElectrode.isActive && z > 0.2 && z < 0.3) {
            double dist = Math.sqrt(x * x + y * y);
            if (dist > FocusingElectrode.getFocusSize() / 2.0) {
                ttl = -1;
            }
        }
        if (HorizontalElectrode.isActive) {
            vx += HorizontalElectrode.getAcceleration() * time;
        }
        if (VerticalElectrode.isActive) {
            vy += VerticalElectrode.getAcceleration() * time;
        }
        if (AcceleratingElectrode.isActive) {
            vz += AcceleratingElectrode.getAcceleration() * time;
        }
        ttl--;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
