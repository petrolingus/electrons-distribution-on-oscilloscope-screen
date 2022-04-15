package me.petrolingus.edos;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import me.petrolingus.edos.core.Pixel;
import me.petrolingus.edos.core.electrongun.ElectronGun;
import me.petrolingus.edos.core.electrongun.PointElectronGun;
import me.petrolingus.edos.core.phys.Electron;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Service extends javafx.concurrent.Service<Void> {

    private final Canvas canvas;

    public Canvas canvasTest;

    public LineChart<Number, Number> currentElectronsChart;

    public boolean clearDistribution = false;

    public Service(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {

            private final List<Pixel> pixels = new ArrayList<>();

            @Override
            protected Void call() {

                double canvasWidth = canvas.getWidth();
                double canvasHeight = canvas.getHeight();

                long lastTimeDraw = System.nanoTime();

                AtomicInteger currentElectrons = new AtomicInteger(0);
                final List<Electron> electrons = new ArrayList<>();
                ElectronGun electronGun = new PointElectronGun();

                AtomicInteger cursor = new AtomicInteger(0);
                List<Integer> deletedElectronsLastMinute = new ArrayList<>();

                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                executor.scheduleAtFixedRate(() -> {

                    if (deletedElectronsLastMinute.size() == 60) {
                        deletedElectronsLastMinute.remove(0);
                    }
                    deletedElectronsLastMinute.add(currentElectrons.get());

                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    for (int i = 0; i < deletedElectronsLastMinute.size(); i++) {
                        int value = deletedElectronsLastMinute.get(i);
                        series.getData().add(new XYChart.Data<>(i, value));
                    }

                    cursor.incrementAndGet();

                    Platform.runLater(() -> {
                        if (currentElectronsChart.getData() != null) {
                            currentElectronsChart.getData().clear();
                        }
                        currentElectronsChart.getData().add(series);
                    });
                }, 0, 1000, TimeUnit.MILLISECONDS);

                int[][] dist = new int[720][720];

//                ScheduledExecutorService executor2 = Executors.newSingleThreadScheduledExecutor();
//                executor2.scheduleAtFixedRate(() -> {
//                    for (int i = 0; i < 720; i++) {
//                        for (int j = 0; j < 720; j++) {
//                            dist[i][j] = 0;
//                        }
//                    }
//                }, 0, 50, TimeUnit.MILLISECONDS);

                while (!isCancelled()) {

                    electrons.add(electronGun.generate());
                    currentElectrons.set(electrons.size());
                    electrons.forEach(Electron::move);

                    // Create pixel when electron ion screen
                    electrons
                            .stream()
                            .filter(Electron::onScreen)
                            .forEach(e -> pixels.add(new Pixel(e.getX(), e.getY())));

                    // Remove electrons
                    electrons.removeIf(Electron::cantMove);

                    // Drawing each 1 ms
                    long nowTimeDraw = System.nanoTime();
                    if (nowTimeDraw - lastTimeDraw > 15_000_000) {
                        Pixel[] pixelsArray = pixels.toArray(new Pixel[0]);
                        Platform.runLater(() -> {
                            GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
                            graphicsContext2D.setFill(Color.BLACK);
                            graphicsContext2D.clearRect(0, 0, canvasWidth, canvasHeight);
                            graphicsContext2D.fillRect(0, 0, canvasWidth, canvasHeight);
                            graphicsContext2D.save();
                            graphicsContext2D.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
                            for (Pixel p : pixelsArray) {
                                double x = canvasWidth * p.getX();
                                double y = canvasHeight * p.getY();
                                if (x > -canvasWidth / 2 && x < canvasWidth / 2 && y > -canvasHeight / 2 && y < canvasHeight / 2) {
                                    int idx = (int) Math.floor(x + canvasWidth / 2.0);
                                    int idy = (int) Math.floor(y + canvasHeight / 2.0);
                                    dist[idy][idx]++;
                                }
                                graphicsContext2D.setFill(Color.BLACK.interpolate(Color.LIGHTGREEN, p.getBrightness()));
                                graphicsContext2D.fillRect(x, y, 1, 1);
                                p.decrementBrightness();
                            }
                            graphicsContext2D.restore();

                            int max = 0;
                            for (int i = 0; i < 720; i++) {
                                for (int j = 0; j < 720; j++) {
                                    if (dist[i][j] > max) {
                                        max = dist[i][j];
                                    }
                                }
                            }

                            double[][] realDist = new double[720][720];
                            for (int i = 0; i < 720; i++) {
                                for (int j = 0; j < 720; j++) {
                                    realDist[i][j] = (double) dist[i][j] / (double) max;
                                }
                            }

                            GraphicsContext g = canvasTest.getGraphicsContext2D();
                            g.clearRect(0, 0, 720, 720);
                            g.setFill(Color.BLACK);
                            g.fillRect(0, 0, 720, 720);
                            if (max > 0) {
                                PixelWriter pixelWriter = g.getPixelWriter();
                                for (int y = 0; y < 720; y++) {
                                    for (int x = 0; x < 720; x++) {
                                        pixelWriter.setColor(x, y, Color.BLACK.interpolate(Color.LIGHTGREEN, realDist[y][x]));
                                    }
                                }
                            }

                        });
                        pixels.removeIf(Pixel::isDead);
                        lastTimeDraw = nowTimeDraw;
                    }

                    if (clearDistribution) {
                        for (int i = 0; i < dist.length; i++) {
                            for (int j = 0; j < dist.length; j++) {
                                dist[i][j] = 0;
                            }
                        }
                    }

                    clearDistribution = false;

                    Thread.yield();
                }

                executor.shutdown();

                System.out.println("STOP");

                return null;
            }
        };
    }
}
