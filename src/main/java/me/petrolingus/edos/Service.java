package me.petrolingus.edos;

import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import me.petrolingus.edos.core.Particle;
import me.petrolingus.edos.core.Pixel;

import java.util.ArrayList;
import java.util.List;

public class Service extends javafx.concurrent.Service<Void> {

    private Canvas canvas;

    public Service(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {

            private final GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();

            private final List<Particle> particles = new ArrayList<>();
            private final List<Pixel> pixels = new ArrayList<>();

            private final double diameter = 1;
            private final double radius = diameter / 2;

            private void fillCircle(double x, double y) {
                graphicsContext2D.fillOval(300 * x - radius, 300 * y - radius, diameter, diameter);
            }

            @Override
            protected Void call() throws Exception {

                long lastTime = System.nanoTime();

                long lastTimeDraw = System.nanoTime();

                while (!isCancelled()) {

                    // Generate new point each 500 us
                    long nowTime = System.nanoTime();
                    if (nowTime - lastTime > 50_000) {
                        Particle particle = new Particle();
                        particles.add(particle);
                        lastTime = nowTime;
                    }

                    // Drawing each 1 sec
                    long nowTimeDraw = System.nanoTime();
                    if (nowTimeDraw - lastTimeDraw > 5_000_000) {
                        graphicsContext2D.setFill(Color.BLACK);
                        graphicsContext2D.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                        graphicsContext2D.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                        graphicsContext2D.save();
                        graphicsContext2D.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
                        for (Pixel p : pixels) {
                            graphicsContext2D.setFill(Color.BLACK.interpolate(Color.LIGHTGREEN, p.getTtlPercent()));
                            fillCircle(p.getX(), p.getY());
                        }
                        graphicsContext2D.restore();
                        lastTimeDraw = nowTimeDraw;
                        pixels.forEach(Pixel::show);
                    }

                    pixels.removeIf(Pixel::isDead);

                    // Move particles
                    for (Particle p : particles) {
                        p.move();
                        if (p.getZ() > 1) {
                            pixels.add(new Pixel(p.getX(), p.getY()));
                            p.ttl = -1;
                        }
                    }
                    particles.removeIf(Particle::isDead);
                }

                System.out.println("STOP");

                return null;
            }
        };
    }
}
