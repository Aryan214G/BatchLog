package com.log.main;

import com.log.ui.splash.SplashScreen;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import com.log.database.DBUtil;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class Main extends Application {

    public Main() throws SQLException {

            System.out.println(DBUtil.getDatabasePath().toAbsolutePath());

        try(Connection conn = DBUtil.getConnection()){

            if (conn != null) {
                System.out.println("Connected to SQLite database!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/log/ui/views/HomePage.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root);

        scene.getStylesheets().add(
                getClass()
                        .getResource("/com/log/ui/styles/homepage.css")
                        .toExternalForm()
        );

        // =========================================
        // Screen Size Handling
        // =========================================

        Rectangle2D screenBounds =
                Screen.getPrimary().getVisualBounds();

        if (screenBounds.getHeight() <= 720) {

            // Small screens (720p laptops)
            stage.setMaximized(true);

        } else {

            // Normal screens
            stage.setWidth(1200);
            stage.setHeight(800);
            stage.centerOnScreen();
        }

        // =========================================
        // Splash Screen
        // =========================================

        SplashScreen splash = new SplashScreen();

        splash.show();

        PauseTransition delay =
                new PauseTransition(Duration.seconds(2));

        delay.setOnFinished(e -> {

            splash.close();

            stage.setTitle("BatchLog");
            stage.setScene(scene);
            stage.show();
        });

        delay.play();
    }

    private void handleJavaFXLogging(){
        Logger rootLogger = Logger.getLogger("");

        for (Handler handler : rootLogger.getHandlers()) {
            handler.setFilter(record -> {
                String msg = record.getMessage();
                if (msg != null && msg.contains("Loading FXML document with JavaFX API of version")) {
                    return false;
                }
                return true;
            });
        }
    }
    public static void main(String[] args) throws SQLException {
        Main obj = new Main();
        obj.handleJavaFXLogging();

        launch();
    }
}

