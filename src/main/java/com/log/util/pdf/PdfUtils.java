package com.log.util.pdf;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;

public class PdfUtils {

    public static void writeText(
        PDPageContentStream content,
        String text,
        float x,
        float y)
        throws IOException {

        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        content.newLineAtOffset(x, y);
        content.showText(text);
        content.endText();
    }

    public static void writeBoldText(
        PDPageContentStream content,
        String text,
        float x,
        float y)
        throws IOException {

        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        content.newLineAtOffset(x, y);
        content.showText(text);
        content.endText();
    }

    public static void writeRightAlignedText(PDPageContentStream content, String text, float rightX, float y) throws IOException {

        PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        float fontSize = 12;

        float textWidth = font.getStringWidth(text) / 1000 * fontSize;

        writeText(content, text, rightX - textWidth, y);
    }

    public static void drawLine(PDPageContentStream content, float startX, float startY, float endX, float endY) throws IOException {

    content.moveTo(startX, startY);
    content.lineTo(endX, endY);
    content.stroke();
}

    // ======================== TABLES ====================================

    public static void drawCell(PDPageContentStream content, float x, float y, float width, float height) throws IOException {

        content.addRect(x, y, width, height);
        content.stroke();
    }

    public static void drawCellText(PDPageContentStream content, String text, float x, float y, float cellWidth, float cellHeight) throws IOException {

    PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    float fontSize = 12;

    float textWidth = font.getStringWidth(text) / 1000 * fontSize;

    float textX = x + (cellWidth - textWidth) / 2;
    float textY = y + (cellHeight - fontSize) / 2 + 4;

    writeText(content, text, textX, textY);
}

    public static void drawCellTextBold(PDPageContentStream content, String text, float x, float y, float cellWidth, float cellHeight) throws IOException {

    PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    float fontSize = 12;

    float textWidth = font.getStringWidth(text) / 1000 * fontSize;

    float textX = x + (cellWidth - textWidth) / 2;
    float textY = y + (cellHeight - fontSize) / 2 + 4;

    writeBoldText(content, text, textX, textY);
}

}
