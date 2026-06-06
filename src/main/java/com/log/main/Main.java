package com.log.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.log.database.DBUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Handler;
import java.util.logging.Logger;

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

//        try {
//
//
//        PdfReportService pdfReportService = new PdfReportService();
//
//        ReportData reportData =
//                pdfReportService.buildReportData(2); // existing Property_ID
//
//        pdfReportService.generatePdf(reportData);
//
//        System.out.println("PDF generated successfully");
//
//    } catch (Exception e) {
//
//        e.printStackTrace();
//    }

        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/com/log/ui/views/HomePage.fxml")
        );

        Parent root = loader.load();
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass()
                .getResource("/com/log/ui/styles/homepage.css").toExternalForm()
        );

        stage.setTitle("BatchLog");
        stage.setScene(scene);
        stage.show();
    }

    private void handleJavaFXLogging(){
        Logger rootLogger = Logger.getLogger("");

        for (Handler handler : rootLogger.getHandlers()) {
            handler.setFilter(record -> {
                String msg = record.getMessage();
                if (msg != null && msg.contains("Loading FXML document with JavaFX API of version")) {
                    return false; // ❌ block this specific warning
                }
                return true; // ✅ allow everything else
            });
        }
    }
    public static void main(String[] args) throws SQLException {
        Main obj = new Main();
        obj.handleJavaFXLogging();

        launch();
    }
}

