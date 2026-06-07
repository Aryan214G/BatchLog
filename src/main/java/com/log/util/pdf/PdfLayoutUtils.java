package com.log.util.pdf;

import org.apache.pdfbox.pdmodel.PDPage;

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
}
