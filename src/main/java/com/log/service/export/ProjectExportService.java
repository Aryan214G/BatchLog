package com.log.service.export;

import com.log.model.BatchRow;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class ProjectExportService {

    private Stage stage;

    private String currentProjectName;



    public void export(Stage stage, List<BatchRow> batches, String currentProjectName) {

        this.stage = stage;
        this.currentProjectName = currentProjectName;

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

        File projectDir = createProjectDir(selectedDirectory);

        // ========= Create batch directory ===========

        createBatchDir(projectDir, batches);

    }

    private File createProjectDir(File selectedDirectory) {



        File projectDir = new File(
                selectedDirectory,
                currentProjectName
        );

        if (!projectDir.exists()) {
            projectDir.mkdirs();
        }

        return projectDir;
    }

    private void createBatchDir(File projectDir, List<BatchRow> batches){

        for (BatchRow batch : batches) {

             File batchDir =
                    new File(
                            projectDir,
                            "Batch_" + batch.getBatchId()
                    );

            if (!batchDir.exists()) {
                batchDir.mkdirs();
            }
        }
    }

}
