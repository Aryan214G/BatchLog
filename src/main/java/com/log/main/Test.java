package com.log.main;
import com.log.dao.BatchDAO;
import com.log.dao.ProductDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public class Test extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        {
            FXMLLoader loader = new FXMLLoader(Test.class.getResource("/com/log/ui/views/settingsPage.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);

            scene.getStylesheets().add(
                    Test.class.getResource("/com/log/ui/styles/baseCategory.css").toExternalForm()
            );

            scene.getStylesheets().add(
                    Test.class.getResource("/com/log/ui/styles/infoBar.css").toExternalForm()
            );

            stage.setTitle("BatchLog");
            stage.setScene(scene);
            stage.show();

            ProductDAO productDAO = new ProductDAO();
//            productDAO.getProductCode(23,"name2",12);
            BatchDAO batchDAO = new BatchDAO();
            //System.out.println(batchDAO.insertBatch(12,"12-12-12","Lab-2",12,productDAO.getProductCode(23,"name2",12)));
        }
    }


    public static void main(String[] args) {
        launch();
    }
}

