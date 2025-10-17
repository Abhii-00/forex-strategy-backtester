package com.forex.backtester;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        Parent root = loader.load();

        // Set initial opacity to 0 (invisible)
        root.setOpacity(0.0);

        // Create a fade transition that lasts 1 second
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), root);
        fadeIn.setFromValue(0.0); // Start from invisible
        fadeIn.setToValue(1.0);   // Fade to fully visible
        fadeIn.play(); // Start the animation

        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setTitle("Strategy Backtester");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}