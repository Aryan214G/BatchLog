package com.log.main;
import com.log.database.DBUtil;
import com.log.util.SearchUtil;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static javafx.application.Application.launch;

public class Test extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        {
//            FXMLLoader loader = new FXMLLoader(Test.class.getResource("/com/log/ui/views/settingsPage.fxml"));
//
//            Parent root = loader.load();
//            Scene scene = new Scene(root);
//
//            scene.getStylesheets().add(
//                    Test.class.getResource("/com/log/ui/styles/baseCategory.css").toExternalForm()
//            );
//
//            scene.getStylesheets().add(
//                    Test.class.getResource("/com/log/ui/styles/infoBar.css").toExternalForm()
//            );
//
//            stage.setTitle("BatchLog");
//            stage.setScene(scene);
//            stage.show();
//
//            ProductDAO productDAO = new ProductDAO();
////            productDAO.getProductCode(23,"name2",12);
//            BatchDAO batchDAO = new BatchDAO();
//            //System.out.println(batchDAO.insertBatch(12,"12-12-12","Lab-2",12,productDAO.getProductCode(23,"name2",12)));

            SearchUtil searchUtil = new SearchUtil();
            Connection connection = DBUtil.getConnection();


            //System.out.println(searchUtil.searchBatches(connection,"","Iphone","","",""));
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

