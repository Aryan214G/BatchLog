package com.log.ui.settings;

import com.log.service.BackupService;
import com.log.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;

public class BackupAndRestorePageController {

    private final BackupService backupService =
            new BackupService();

    @FXML
    private void handleCreateBackup() {

        try {

            FileChooser chooser =
                    new FileChooser();

            chooser.setTitle(
                    "Save Database Backup"
            );

            chooser.setInitialFileName(
                    "BatchLog_Backup.db"
            );

            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Database Files",
                            "*.db"
                    )
            );

            File file =
                    chooser.showSaveDialog(null);

            if (file == null) {
                return;
            }

            backupService.createBackup(
                    file.toPath()
            );

            AlertUtil.showInfo(
                    "Backup created successfully."
            );

        } catch (Exception e) {

            e.printStackTrace();

            AlertUtil.showError(
                    "Backup Failed"
            );
        }
    }

    @FXML
    private void handleRestoreBackup() {

        try {

            boolean confirmed =
                AlertUtil.showConfirmation(
                        """
                        This will replace the current database.
        
                        A safety backup will be created automatically.
        
                        Continue?
                        """
                );

        if (!confirmed) {
            return;
        }

            FileChooser chooser =
                    new FileChooser();

            chooser.setTitle(
                    "Select Backup File"
            );

            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Database Files",
                            "*.db"
                    )
            );

            File file =
                    chooser.showOpenDialog(null);

            if (file == null) {
                return;
            }

            backupService.restoreBackup(
                    file.toPath()
            );

            AlertUtil.showInfo(
                    """
                    Restore Complete
                    
                    Please restart BatchLog.
                    """
            );

        } catch (Exception e) {

            e.printStackTrace();

            AlertUtil.showError("Restore Failed");
        }
    }
}