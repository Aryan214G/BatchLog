package com.log.util.pdf;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;

public final class PdfLayoutUtils {

    private PdfLayoutUtils() {}

    public static float getLeftMargin(PDPage page) {
        return page.getMediaBox().getWidth() * 0.08f;
    }

    public static float getRightMargin(PDPage page) {
        return page.getMediaBox().getWidth() * 0.92f;
    }

    public static float getPageCenterX(PDPage page) {
        return page.getMediaBox().getWidth() / 2;
    }

    public static float getCenteredTextX(
            PDPage page,
            String text,
            PDFont font,
            float fontSize
    ) throws IOException {

        float textWidth =
                font.getStringWidth(text) / 1000 * fontSize;

        return (page.getMediaBox().getWidth() - textWidth) / 2;
    }
}
