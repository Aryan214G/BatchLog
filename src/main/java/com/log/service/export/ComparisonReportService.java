package com.log.service.export;

import com.log.dto.ComparisonReportData;
import com.log.util.DateUtils;
import com.log.util.DialogUtils;
import com.log.util.pdf.PdfConstants;
import com.log.util.pdf.PdfLayoutUtils;
import com.log.util.pdf.PdfUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.log.util.pdf.PdfUtils.*;

public class ComparisonReportService
        implements ReportGenerator<ComparisonReportData> {

    @Override
    public void generateReport(
            ComparisonReportData data
    ) throws Exception {

        Optional<String> result =
                DialogUtils.showTextInputDialog(
                        "Report Number",
                        "Generate Report",
                        "Enter report number:"
                );

        String reportNumber =
                result.orElse("");

        try (PDDocument document = new PDDocument()) {

    List<Map.Entry<String, List<String>>> rows =
            new ArrayList<>(data.getRows().entrySet());

    int startRow = 0;

    while (startRow < rows.size()) {

        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream content =
                     new PDPageContentStream(document, page)) {

            float y = PdfConstants.TOP_Y;

            y = drawReportHeader(
                    page,
                    content,
                    reportNumber,
                    y
            );

            float leftMargin =
                    PdfLayoutUtils.getLeftMargin(page);

            float rightMargin =
                    PdfLayoutUtils.getRightMargin(page);

            y -= PdfConstants.SECTION_SPACING;

            startRow = drawComparisonTable(
                    content,
                    data,
                    y,
                    leftMargin,
                    rightMargin,
                    startRow
            );
        }
    }

    document.save("comparison-report.pdf");
}
    }

    private float drawReportHeader(
            PDPage page,
            PDPageContentStream content,
            String reportNumber,
            float y
    ) throws Exception {

        String title = "COMPARISON REPORT";

        float leftMargin =
                PdfLayoutUtils.getLeftMargin(page);

        float rightMargin =
                PdfLayoutUtils.getRightMargin(page);

        float titleX =
                PdfLayoutUtils.getCenteredTextX(
                        page,
                        title,
                        PdfUtils.BOLD_FONT,
                        PdfConstants.FONT_SIZE
                );

        writeBoldText(
                content,
                title,
                titleX,
                y
        );

        y -= PdfConstants.SECTION_SPACING;

        writeText(
                content,
                "Report Number: " + reportNumber,
                leftMargin,
                y
        );

        writeRightAlignedText(
                content,
                "Date: " + DateUtils.getCurrentDateFormatted(),
                rightMargin,
                y
        );

        y -= PdfConstants.SECTION_SPACING;

        return y;
    }

    private int drawComparisonTable(
            PDPageContentStream content,
            ComparisonReportData data,
            float y,
            float leftMargin,
            float rightMargin,
            int startRow
    ) throws Exception {

        float tableX = leftMargin;
        float tableY = y;

        float rowHeight = 45;

        List<String> headers =
                new ArrayList<>();

        headers.add("Property");
        headers.addAll(data.getColumnHeaders());

        float availableWidth = rightMargin - leftMargin;

        float propertyWidth = availableWidth * 0.30f;

        float valueWidth =
                (availableWidth - propertyWidth)
                        / data.getColumnHeaders().size();

        // ====================================
        // HEADER ROW
        // ====================================

        float currentX = tableX;

        for (String header : headers) {

            float width =
                    header.equals("Property")
                            ? propertyWidth
                            : valueWidth;

            drawCell(
                    content,
                    currentX,
                    tableY,
                    width,
                    rowHeight
            );

            drawCellTextBold(
                    content,
                    header,
                    currentX,
                    tableY,
                    width,
                    rowHeight
            );

            currentX += width;
        }

        tableY -= rowHeight;

        // ====================================
        // DATA ROWS
        // ====================================

        List<Map.Entry<String, List<String>>> rows =
        new ArrayList<>(data.getRows().entrySet());

int rowIndex = startRow;

while (rowIndex < rows.size()) {

    if (tableY - rowHeight <
            PdfConstants.BOTTOM_MARGIN) {

        return rowIndex;
    }

    Map.Entry<String, List<String>> row =
            rows.get(rowIndex);

    currentX = tableX;

    drawCell(
            content,
            currentX,
            tableY,
            propertyWidth,
            rowHeight
    );

    drawWrappedCellText(
            content,
            row.getKey(),
            currentX,
            tableY,
            propertyWidth,
            rowHeight
    );

    currentX += propertyWidth;

    for (String value : row.getValue()) {

        drawCell(
                content,
                currentX,
                tableY,
                valueWidth,
                rowHeight
        );

        drawCellText(
                content,
                value,
                currentX,
                tableY,
                valueWidth,
                rowHeight
        );

        currentX += valueWidth;
    }

    tableY -= rowHeight;

    rowIndex++;
}

return rowIndex;
    }
}