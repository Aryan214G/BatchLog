package com.log.service;

import com.log.database.DBUtil;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupService {

    private static final String SAFETY_BACKUP_DIR =
            "backups/safety-backups";

    public void createBackup(Path destination)
            throws IOException {

        Files.copy(
                DBUtil.getDatabasePath(),
                destination,
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    public void restoreBackup(Path backupFile)
            throws IOException {

        createSafetyBackup();

        Files.copy(
                backupFile,
                DBUtil.getDatabasePath(),
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    private Path createSafetyBackup()
            throws IOException {

        Path backupDir =
                Paths.get(SAFETY_BACKUP_DIR);

        Files.createDirectories(backupDir);

        String timestamp =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd_HH-mm-ss"
                                )
                        );

        Path safetyBackup =
                backupDir.resolve(
                        "BeforeRestore_" +
                        timestamp +
                        ".db"
                );

        Files.copy(
                DBUtil.getDatabasePath(),
                safetyBackup,
                StandardCopyOption.REPLACE_EXISTING
        );

        return safetyBackup;
    }
}