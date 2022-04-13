package me.petrolingus.edos;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Controller {

    public Canvas canvas;

    private Service service;

    public void initialize() {

        service = new Service(canvas);

        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        graphicsContext2D.setFill(Color.BLACK);
        graphicsContext2D.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    }

    public void onStart() {
        if (!service.isRunning()) {
            service.start();
        }
    }

    public void onStop() {
        if (service.isRunning()) {
            service.cancel();
            service.reset();
        }
    }

}
