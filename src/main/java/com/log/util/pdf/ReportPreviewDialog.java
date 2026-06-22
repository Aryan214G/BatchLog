package com.log.util.pdf;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;

public class ReportPreviewDialog {


private ReportPreviewDialog() {
}

public static void show(
        PDDocument document
) {

    try {

        PDFRenderer renderer =
                new PDFRenderer(document);

        VBox pages =
                new VBox(20);

        pages.setPadding(
                new Insets(20)
        );

        for (int i = 0;
             i < document.getNumberOfPages();
             i++) {

            BufferedImage image =
                    renderer.renderImageWithDPI(
                            i,
                            150
                    );

            ImageView imageView =
                    new ImageView(
                            SwingFXUtils.toFXImage(
                                    image,
                                    null
                            )
                    );

            imageView.setPreserveRatio(true);
            imageView.setFitWidth(800);

            pages.getChildren().add(
                    imageView
            );
        }

        Platform.runLater(() -> {

            try {

                ScrollPane scrollPane =
                        new ScrollPane(pages);

                scrollPane.setFitToWidth(true);

                Button printButton =
                        new Button("Print");

                Stage stage =
                        new Stage();

                printButton.setOnAction(event -> {

                    try {

                        PrinterJob job =
                                PrinterJob.getPrinterJob();

                        job.setPageable(
                                new PDFPageable(document)
                        );

                        if (job.printDialog()) {

                            job.print();

                            stage.close();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                HBox buttonBar =
                        new HBox(printButton);

                buttonBar.setAlignment(
                        Pos.CENTER_RIGHT
                );

                buttonBar.setPadding(
                        new Insets(10)
                );

                BorderPane root =
                        new BorderPane();

                root.setCenter(scrollPane);
                root.setBottom(buttonBar);

                stage.setTitle(
                        "Report Preview"
                );

                stage.setScene(
                        new Scene(
                                root,
                                900,
                                1000
                        )
                );

                stage.setOnHidden(event -> {

                    try {
                        document.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                stage.show();

            } catch (Exception e) {

                e.printStackTrace();

                try {
                    document.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    } catch (Exception e) {

        e.printStackTrace();

        try {
            document.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


}
