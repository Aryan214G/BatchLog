package com.log.service;

import com.log.database.DBUtil;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupService {

    private static final Path SAFETY_BACKUP_DIR = DBUtil.getAppDir().resolve(
            Paths.get("database", "backups")
    );

    public void createBackup(Path destination)
            throws IOException {

        if (!Files.exists(DBUtil.getDatabasePath())) {
            throw new IOException("Database file not found.");
        }

        Files.copy(
                DBUtil.getDatabasePath(),
                destination,
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    public void restoreBackup(Path backupFile)
            throws IOException {

        if (!Files.exists(backupFile)) {
            throw new IOException("Backup file not found.");
        }
        if (!backupFile.toString().toLowerCase().endsWith(".db")) {
            throw new IOException("Selected file is not a database backup.");
        }

        createSafetyBackup();

        Files.copy(
                backupFile,
                DBUtil.getDatabasePath(),
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    private Path createSafetyBackup()
            throws IOException {

        Files.createDirectories(SAFETY_BACKUP_DIR);

        String timestamp =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd_HH-mm-ss"
                                )
                        );

        Path safetyBackup =
                SAFETY_BACKUP_DIR.resolve(
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