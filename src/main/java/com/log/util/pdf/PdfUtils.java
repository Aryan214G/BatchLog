package com.log.util.pdf;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfUtils {

    public static final PDFont NORMAL_FONT =
        new PDType1Font(
                Standard14Fonts.FontName.HELVETICA
        );

    public static final PDFont BOLD_FONT =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA_BOLD
            );

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

        PDFont font = NORMAL_FONT;
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

    PDFont font = BOLD_FONT;
    float fontSize = PdfConstants.FONT_SIZE;

    float textWidth = font.getStringWidth(text) / 1000 * fontSize;

    float textX = x + (cellWidth - textWidth) / 2;
    float textY = y + (cellHeight - fontSize) / 2 + 4;

    writeBoldText(content, text, textX, textY);
}

    public static List<String> wrapText(
            String text,
            PDFont font,
            float fontSize,
            float maxWidth
    ) throws IOException {

        List<String> lines = new ArrayList<>();

        String[] words = text.split("\\s+");

        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {

            String candidate =
                    currentLine.isEmpty()
                            ? word
                            : currentLine + " " + word;

            float width =
                    font.getStringWidth(candidate)
                            / 1000f
                            * fontSize;

            if (width <= maxWidth) {

                currentLine.setLength(0);
                currentLine.append(candidate);

            } else {

                if (!currentLine.isEmpty()) {
                    lines.add(currentLine.toString());
                }

                currentLine.setLength(0);
                currentLine.append(word);
            }
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    public static void drawWrappedCellText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            float width,
            float height
    ) throws IOException {

        float fontSize = PdfConstants.FONT_SIZE;

        List<String> lines =
                wrapText(
                        text,
                        PdfUtils.NORMAL_FONT,
                        fontSize,
                        width - 8
                );

        while (
                lines.size() * fontSize > height - 4
                && fontSize > 6
        ) {

            fontSize -= 1;

            lines = wrapText(
                    text,
                    PdfUtils.NORMAL_FONT,
                    fontSize,
                    width - 8
            );
        }

        float totalTextHeight =
                lines.size() * fontSize;

        float startY =
                y + (height - totalTextHeight) / 2f;

        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i);

            float textWidth =
                    PdfUtils.NORMAL_FONT
                            .getStringWidth(line)
                            / 1000f
                            * fontSize;

            float textX =
                    x + (width - textWidth) / 2f;

            float textY =
                    startY + (lines.size() - i - 1) * fontSize;

            content.beginText();
            content.setFont(
                    PdfUtils.NORMAL_FONT,
                    fontSize
            );
            content.newLineAtOffset(
                    textX,
                    textY
            );
            content.showText(line);
            content.endText();
        }
    }
}
