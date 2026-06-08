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
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.printing.PDFPageable;

import java.awt.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.*;
import java.util.List;

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

            PDPage page = new PDPage(PDRectangle.A4);
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

                drawLine(content, leftMargin, y, rightMargin, y);

                y -= PdfConstants.LINE_SPACING;
                // ========= TABLE SECTION =============================================

                drawTableSection(
                        content,
                        data,
                        y,
                        leftMargin,
                        rightMargin
                );


            }
            document.save("retrieval-report.pdf");

            printReport(document);
        }
    }

    private void printReport(PDDocument document){

        try {

            PrinterJob job = PrinterJob.getPrinterJob();


            job.setPageable(new PDFPageable(document));

            boolean accepted = job.printDialog();

            System.out.println("Dialog result = " + accepted);

            if (accepted) {
                System.out.println(job.getPrintService());
                job.print();
                System.out.println("Print done");
            }
        } catch (HeadlessException e) {
            throw new RuntimeException(e);
        } catch (PrinterException e) {
            throw new RuntimeException(e);
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


        private void drawTableSection(
        PDPageContentStream content,
        RetrievalTableReportData data,
        float y,
        float leftMargin,
        float rightMargin
        ) throws IOException {

        float tableX = leftMargin;
                float tableY = y - PdfConstants.SECTION_SPACING;

                float rowHeight = PdfTableConstants.ROW_HEIGHT;

                List<String> headers = data.getHeaders();

                int propertyIndex = headers.indexOf("Property");

                // ================= COLUMN WIDTHS =================

                Map<String, Float> weights = new HashMap<>();

                weights.put("Property", 3f);
                weights.put("Temperature", 2f);
                weights.put("Direction", 2f);
                weights.put("Average Value", 1.5f);
                weights.put("Average", 1.5f);

                float availableWidth = rightMargin - leftMargin;

                float totalWeight = 0;

                for (String header : headers) {

                    if (header.startsWith("Value")) {
                        totalWeight += 1f;
                    } else {
                        totalWeight += weights.getOrDefault(header, 1f);
                    }
                }

                Map<String, Float> columnWidths = new LinkedHashMap<>();

                for (String header : headers) {

                    float weight;

                    if (header.startsWith("Value")) {
                        weight = 1f;
                    } else {
                        weight = weights.getOrDefault(header, 1f);
                    }

                    float width =
                            availableWidth * (weight / totalWeight);

                    columnWidths.put(header, width);
                }

                for (Map.Entry<String, List<List<String>>> section
        : data.getGroupedRows().entrySet()) {

                // =====================================================
                // CATEGORY ROW (merged cell)
                // =====================================================

                drawCell(
                        content,
                        tableX,
                        tableY,
                        availableWidth,
                        rowHeight
                );

                drawCellTextBold(
                        content,
                        section.getKey().toUpperCase(),
                        tableX,
                        tableY,
                        availableWidth,
                        rowHeight
                );

                tableY -= rowHeight;

                // =====================================================
                // HEADER ROW
                // =====================================================

                float currentX = tableX;

                for (String header : headers) {

                    float width = columnWidths.get(header);

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

                // =====================================================
                // DATA ROWS (MERGED PROPERTY CELLS)
                // =====================================================

                List<List<String>> rows = section.getValue();

                int rowIndex = 0;

                while (rowIndex < rows.size()) {

                    List<String> firstRow = rows.get(rowIndex);

                    String propertyText =
                            firstRow.get(propertyIndex);

                    int spanCount = 1;

                    while (
                            rowIndex + spanCount < rows.size()
                            &&
                            rows.get(rowIndex + spanCount)
                                    .get(propertyIndex)
                                    .equals(propertyText)
                    ) {
                        spanCount++;
                    }

                    // ==========================================
                    // PROPERTY CELL
                    // ==========================================

                    float propertyX = tableX;

                    for (int i = 0; i < propertyIndex; i++) {
                        propertyX += columnWidths.get(headers.get(i));
                    }

                    float propertyWidth =
                            columnWidths.get(headers.get(propertyIndex));

                    float mergedHeight =
                            spanCount * rowHeight;

                    float mergedY =
                            tableY - mergedHeight + rowHeight;

                    drawCell(
                            content,
                            propertyX,
                            mergedY,
                            propertyWidth,
                            mergedHeight
                    );

                    drawWrappedCellText(
                            content,
                            propertyText,
                            propertyX,
                            mergedY,
                            propertyWidth,
                            mergedHeight
                    );

                    // ==========================================
                    // OTHER CELLS
                    // ==========================================

                    for (int offset = 0; offset < spanCount; offset++) {

                        List<String> row =
                                rows.get(rowIndex + offset);

                        float currentRowY =
                                tableY - (offset * rowHeight);

                        currentX = tableX;

                        for (int col = 0; col < headers.size(); col++) {

                            String header = headers.get(col);

                            float width =
                                    columnWidths.get(header);

                            if (col == propertyIndex) {

                                currentX += width;
                                continue;
                            }

                            drawCell(
                                    content,
                                    currentX,
                                    currentRowY,
                                    width,
                                    rowHeight
                            );

                            drawCellText(
                                    content,
                                    row.get(col),
                                    currentX,
                                    currentRowY,
                                    width,
                                    rowHeight
                            );

                            currentX += width;
                        }
                    }

                    tableY -= spanCount * rowHeight;

                    rowIndex += spanCount;
                }

                tableY -= PdfConstants.SECTION_SPACING;
            }
        }
}
