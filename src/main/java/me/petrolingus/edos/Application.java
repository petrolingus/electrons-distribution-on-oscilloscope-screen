package me.petrolingus.edos;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) {

        Label label = new Label("Hello, World!");
        Scene scene = new Scene(new StackPane(label), 640, 480);

        primaryStage.setTitle("EDOS");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
