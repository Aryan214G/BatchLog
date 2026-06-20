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

    private static final boolean TESTING = true;

    private static final Path APP_DIR =
        Paths.get(
                System.getenv("LOCALAPPDATA"),
                "BatchLog"
        );

    private static final Path DB_PATH = (TESTING)
        ? Paths.get("database", "BatchLog_test.db")
        : APP_DIR.resolve("BatchLog.db");

    private static final String URL = "jdbc:sqlite:" + DB_PATH.toAbsolutePath();


    static {

    try {

        if (!TESTING) {

            Files.createDirectories(APP_DIR);

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