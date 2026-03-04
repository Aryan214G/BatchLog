package com.log.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.log.database.DBUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {

    public Main() throws SQLException {
        try(Connection conn = DBUtil.getConnection()){

            if (conn != null) {
                System.out.println("Connected to SQLite database!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/com/log/ui/views/CategoriesPage.fxml")
        );

        Parent root = loader.load();
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass()
                .getResource("/com/log/ui/styles/categoriesPage.css").toExternalForm()
        );

        stage.setTitle("BatchLog");
        stage.setScene(scene);
        stage.show();
    }






    public static void main(String[] args) {
        launch();
    }
}

