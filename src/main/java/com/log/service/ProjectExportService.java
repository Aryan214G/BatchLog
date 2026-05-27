package com.log.service;

import com.log.model.Batch;
import com.log.model.BatchRow;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class ProjectExportService {

    private Stage stage;

    public void export(Stage stage, List<BatchRow> batches) {

        this.stage = stage;

        createDirectories(batches);
    }

    public void createDirectories(List<BatchRow> batches){
        DirectoryChooser directoryChooser =
        new DirectoryChooser();

        directoryChooser.setTitle("Select Export Location");

        File selectedDirectory = directoryChooser.showDialog(stage);

         if (selectedDirectory == null) {
        return;
    }

    System.out.println(
            "Selected folder: "
            + selectedDirectory.getAbsolutePath()
    );

         // ========= Create project directory ===========

        File projectDir = null;
        createProjectDir(selectedDirectory, projectDir);

        // ========= Create batch directory ===========

        File batchDir = null;

        createBatchDir(projectDir, batchDir, batches);

    }

    private void createProjectDir(File selectedDirectory, File projectDir) {

        String projectName = "Project_1";

        projectDir = new File(
                selectedDirectory,
                projectName
        );

        if (!projectDir.exists()) {
            projectDir.mkdirs();
        }
    }

    private void createBatchDir(File projectDir, File batchDir, List<BatchRow> batches){

        for (BatchRow batch : batches) {

             batchDir =
                    new File(
                            projectDir,
                            "Batch_" + batch.getBatchCode()
                    );

            if (!batchDir.exists()) {
                batchDir.mkdirs();
            }
        }
    }

}
