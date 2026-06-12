package com.log.util;

import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class DialogUtils {

    private DialogUtils() {
        // Prevent instantiation
    }

    public static Optional<String> showTextInputDialog(
        String title,
        String header,
        String contentText) {

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(contentText);

        return dialog.showAndWait();
    }
}
