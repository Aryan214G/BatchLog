package com.log.service.export;

import com.log.dto.RetrievalTableReportData;
import com.log.util.DateUtils;
import com.log.util.DialogUtils;
import com.log.util.pdf.PdfConstants;
import com.log.util.pdf.PdfLayoutUtils;
import com.log.util.pdf.PdfTableConstants;
import com.log.util.pdf.PdfUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.log.util.pdf.PdfUtils.*;

public class RetrievalTablePdfService
        implements Exporter<RetrievalTableReportData> {

    @Override
    public void export(RetrievalTableReportData data)
            throws Exception {

        Optional<String> result = DialogUtils.showTextInputDialog(
        "Report Number",
        "Generate Report",
        "Enter report number:"
    );
        String reportNumber = result.orElse(null);


        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream content =
                         new PDPageContentStream(document, page)) {

                String title = "TEST REPORT";

                 // ========= CONSTANTS =============================================
                float y = PdfConstants.TOP_Y;
                float leftMargin = PdfLayoutUtils.getLeftMargin(page);
                float titleX = PdfLayoutUtils.getCenteredTextX(
                        page,
                        title,
                        PdfUtils.BOLD_FONT,
                        PdfConstants.FONT_SIZE
                );
                float rightMargin = PdfLayoutUtils.getRightMargin(page);

                 // ========= HEADER SECTION =============================================

                y = drawReportHeader(
                        page,
                        content,
                        data,
                        reportNumber,
                        y,
                        leftMargin,
                        titleX,
                        rightMargin,
                        title
                );

                // ========= TABLE SECTION =============================================

                // TABLE CONSTANTS

                float tableX = leftMargin;
                float tableY = y;

                float rowHeight = PdfTableConstants.ROW_HEIGHT;
                float columnWidth = PdfTableConstants.DEFAULT_COLUMN_WIDTH;

                for (Map.Entry<String, List<List<String>>> section
        : data.getGroupedRows().entrySet()) {

                // =====================================================
                // CATEGORY ROW (merged cell)
                // =====================================================

                float tableWidth =
                        data.getHeaders().size() * columnWidth;

                drawCell(
                        content,
                        tableX,
                        tableY,
                        tableWidth,
                        rowHeight
                );

                drawCellTextBold(
                        content,
                        section.getKey().toUpperCase(),
                        tableX,
                        tableY,
                        tableWidth,
                        rowHeight
                );

                tableY -= rowHeight;

                // =====================================================
                // COLUMN HEADERS
                // =====================================================

                float currentX = tableX;

                for (String header : data.getHeaders()) {

                    drawCell(
                            content,
                            currentX,
                            tableY,
                            columnWidth,
                            rowHeight
                    );

                    drawCellTextBold(
                            content,
                            header,
                            currentX,
                            tableY,
                            columnWidth,
                            rowHeight
                    );

                    currentX += columnWidth;
                }

                tableY -= rowHeight;

                // =====================================================
                // DATA ROWS
                // =====================================================

                for (List<String> row : section.getValue()) {

                    currentX = tableX;

                    for (String cell : row) {

                        drawCell(
                                content,
                                currentX,
                                tableY,
                                columnWidth,
                                rowHeight
                        );

                        drawCellText(
                                content,
                                cell,
                                currentX,
                                tableY,
                                columnWidth,
                                rowHeight
                        );

                        currentX += columnWidth;
                    }

                    tableY -= rowHeight;
                }

                // =====================================================
                // SPACE BEFORE NEXT CATEGORY
                // =====================================================

                tableY -= PdfConstants.SECTION_SPACING;
            }

            }
            document.save("retrieval-report.pdf");
        }
    }

    private float drawReportHeader(
            PDPage page,
            PDPageContentStream content,
            RetrievalTableReportData data,
            String reportNumber,
            float y,
            float leftMargin,
            float titleX,
            float rightMargin,
            String title
    ) throws IOException {

        writeBoldText(content, title, titleX, y);

                y -= PdfConstants.SECTION_SPACING;

                writeText(content, "Report number: " + reportNumber,
                        leftMargin, y);

                writeRightAlignedText(content, "Date: " + DateUtils.getCurrentDateFormatted(),
                        rightMargin, y);

                y -= PdfConstants.SECTION_SPACING;

                writeText(
                        content,
                        "Project name: " + data.getProjectName(),
                        leftMargin,
                        y
                );

                y -= PdfConstants.LINE_SPACING;

                writeText(
                        content,
                        "Product: " + data.getProduct(),
                        leftMargin,
                        y
                );



                y -= PdfConstants.LINE_SPACING;

                if(data.getBatchNo() != null){

                    writeText(content, "Batch No.: " + data.getBatchNo(),
                            leftMargin,
                            y);

                    y -= PdfConstants.LINE_SPACING;
                }
                writeText(
                        content,
                        "Component ID: "+ data.getComponentId(),
                        leftMargin,
                        y
                );

                y -= PdfConstants.LINE_SPACING;

                writeText(
                        content,
                        "SOP: " + data.getSop(),
                        leftMargin,
                        y
                );

                y -= PdfConstants.LINE_SPACING;

                writeText(
                        content,
                        "Test Schedule: " + data.getTestSchedule(),
                        leftMargin,
                        y
                );

                y -= PdfConstants.SECTION_SPACING;

                return y;

        }
}
