package me.petrolingus.edos.core.electrongun;

import me.petrolingus.edos.core.math.Constants;
import me.petrolingus.edos.core.phys.Electron;

import java.util.concurrent.ThreadLocalRandom;

public class PointElectronGun implements ElectronGun {

    public static double speed = 1.3e7;
    public static double theta = 0;

    @Override
    public Electron generate() {
        double thetaInRadians = Math.toRadians(theta);
        double th = ThreadLocalRandom.current().nextDouble(-thetaInRadians, thetaInRadians);
        double phi = Constants.PI2 * ThreadLocalRandom.current().nextDouble();
        double vx = speed * Math.sin(th) * Math.cos(phi);
        double vy = speed * Math.sin(th) * Math.sin(phi);
        double vz = speed * Math.cos(th);
        return new Electron(vx, vy, vz);
    }
}
