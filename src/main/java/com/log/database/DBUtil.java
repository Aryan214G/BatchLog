package com.log.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    private static final boolean TESTING = false;

    private static final Path APP_DIR;
    private static final Path DB_PATH;
    private static final String URL;

    static {

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {

            APP_DIR = Paths.get(
                    System.getenv("LOCALAPPDATA"),
                    "BatchLog"
            );

        } else if (os.contains("mac")) {

            APP_DIR = Paths.get(
                    System.getProperty("user.home"),
                    "Library",
                    "Application Support",
                    "BatchLog"
            );

        } else {

            APP_DIR = Paths.get(
                    System.getProperty("user.home"),
                    ".batchlog"
            );
        }

        DB_PATH = TESTING
                ? Paths.get("database", "BatchLog_test.db")
                : APP_DIR.resolve("database")
                .resolve("BatchLog.db");

        URL = "jdbc:sqlite:" + DB_PATH.toAbsolutePath();

        try {

            if (!TESTING) {

                Files.createDirectories(
                        APP_DIR.resolve("database")
                );

                if (!Files.exists(DB_PATH)) {
                    copyMasterDatabase();
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to initialize database",
                    e
            );
        }
    }

//    private static final Path DB_PATH = (TESTING)
//        ? Paths.get("database", "BatchLog_test.db")
//        : APP_DIR.resolve(
//                Paths.get("database", "BatchLog.db")
//    );
//
//    private static final String URL = "jdbc:sqlite:" + DB_PATH.toAbsolutePath();


    static {

    try {

        if (!TESTING) {

            Files.createDirectories(
                    APP_DIR.resolve("database")
            );

            if (!Files.exists(DB_PATH)) {
                copyMasterDatabase();
            }
        }

    } catch (Exception e) {

        throw new RuntimeException(
                "Failed to initialize database",
                e
        );
    }
}

    public static Connection getConnection() throws SQLException {

        Connection conn = DriverManager.getConnection(URL);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        return conn;
    }

    public static Path getDatabasePath() {
        return DB_PATH;
    }

    public static Path getAppDir() {
        return APP_DIR;
    }

    private static void copyMasterDatabase() throws IOException {

    try (InputStream is =
                 DBUtil.class.getResourceAsStream("/database/BatchLog.db")) {

        if (is == null) {
            throw new FileNotFoundException("Master database not found");
        }

        Files.copy(
                is,
                DB_PATH,
                StandardCopyOption.REPLACE_EXISTING
        );
    }
}

}