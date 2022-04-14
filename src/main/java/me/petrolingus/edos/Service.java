package me.petrolingus.edos;

import javafx.application.Platform;
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

//            private final GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();

            private final List<Particle> particles = new ArrayList<>();
            private final List<Pixel> pixels = new ArrayList<>();

            private final double diameter = 1;
            private final double radius = diameter / 2;

//            private void fillCircle(double x, double y) {
//                graphicsContext2D.fillOval(300 * x - radius, 300 * y - radius, diameter, diameter);
//            }

            @Override
            protected Void call() throws Exception {

                long lastTime = System.nanoTime();

                long lastTimeDraw = System.nanoTime();

                double t = 0;
                double t2 = 0;

                while (!isCancelled()) {

                    // Generate new point each 5 us
                    long nowTime = System.nanoTime();
                    if (nowTime - lastTime > 500) {
                        Particle particle = new Particle();
                        particles.add(particle);
                        lastTime = nowTime;
                    }

                    // Drawing each 5 ms
                    long nowTimeDraw = System.nanoTime();
                    if (nowTimeDraw - lastTimeDraw > 5_000_000) {

                        Pixel[] pixels1 = pixels.toArray(new Pixel[0]);

                        Platform.runLater(()->{

                            GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
                            graphicsContext2D.setFill(Color.BLACK);
                            graphicsContext2D.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                            graphicsContext2D.save();
                            graphicsContext2D.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
                            for (Pixel p : pixels1) {
                                graphicsContext2D.setFill(Color.BLACK.interpolate(Color.LIGHTGREEN, p.getTtlPercent()));
                                graphicsContext2D.fillOval(300 * p.getX() - radius, 300 * p.getY() - radius, diameter, diameter);
                            }
                            graphicsContext2D.restore();
                        });
                        lastTimeDraw = nowTimeDraw;
                        pixels.forEach(Pixel::show);
                    }

                    pixels.removeIf(Pixel::isDead);

                    t += 0.1;
                    t2 += 0.000001;
                    Particle.horizontalB = 0.8 * Math.cos(0.1 * t + t2);
                    Particle.verticalB = 0.8 * Math.cos(t + t2);

                    // Move particles
                    for (Particle p : particles) {
                        p.move();
                        if (p.getZ() > 1) {
                            pixels.add(new Pixel(p.getX(), p.getY()));
                            p.ttl = -1;
                        }
                    }
                    particles.removeIf(Particle::isDead);

                    Thread.yield();
                }

                System.out.println("STOP");

                return null;
            }
        };
    }
}
