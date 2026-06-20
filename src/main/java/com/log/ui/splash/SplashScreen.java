package com.log.ui.splash;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class SplashScreen {

    private final Stage stage;

    public SplashScreen() {

        stage = new Stage(StageStyle.UNDECORATED);

        Image image = new Image(
        Objects.requireNonNull(
                getClass().getResourceAsStream(
                        "/images/BatchLog_splash_screen.png"
                )
        )
);

        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(900);
        imageView.setPreserveRatio(true);

        StackPane root = new StackPane(imageView);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);

    }

    public void show() {
        stage.centerOnScreen();
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
