package com.log.service.export;

import com.log.dto.RetrievalTableReportData;
import com.log.util.DateUtils;
import com.log.util.DialogUtils;
import com.log.util.pdf.*;
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

public class RetrievalTableReportService
        implements ReportGenerator<RetrievalTableReportData> {

        @Override
    public void generateReport(RetrievalTableReportData data)
            throws Exception {

        Optional<String> result = DialogUtils.showTextInputDialog(
                "Report Number",
                "Generate Report",
                "Enter report number:"
        );

        String reportNumber = result.orElse(null);

        try (PDDocument document = new PDDocument()) {

            String title = "TEST REPORT";

            PdfPageContext context =
                    createNewPage(document);

            float leftMargin =
                    PdfLayoutUtils.getLeftMargin(
                            context.getPage()
                    );

            float rightMargin =
                    PdfLayoutUtils.getRightMargin(
                            context.getPage()
                    );

            float titleX =
                    PdfLayoutUtils.getCenteredTextX(
                            context.getPage(),
                            title,
                            PdfUtils.BOLD_FONT,
                            PdfConstants.FONT_SIZE
                    );

            float y = drawReportHeader(
                    context.getPage(),
                    context.getContent(),
                    data,
                    reportNumber,
                    PdfConstants.TOP_Y,
                    leftMargin,
                    titleX,
                    rightMargin,
                    title
            );

            drawLine(
                    context.getContent(),
                    leftMargin,
                    y,
                    rightMargin,
                    y
            );

            context.setCurrentY(
                    y - PdfConstants.LINE_SPACING
            );

            context = drawTableSection(
                    document,
                    context,
                    data,
                    leftMargin,
                    rightMargin
            );

            context.getContent().close();

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

                    writeText(content, "Batch ID.: " + data.getBatchNo(),
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

                if(data.getSop() != null) {
                    writeText(content, "SOP: " + data.getSop(), leftMargin, y);
                }

                y -= PdfConstants.LINE_SPACING;

                if(data.getTestSchedule() != null){
                    writeText(content, "Test Schedule: " + data.getTestSchedule(), leftMargin, y);
                }

                y -= PdfConstants.SECTION_SPACING;

                return y;

        }


        private PdfPageContext drawTableSection(
                PDDocument document,
        PdfPageContext context,
        RetrievalTableReportData data,
        float leftMargin,
        float rightMargin
        ) throws IOException {

        float tableX = leftMargin;
        context.setCurrentY(context.getCurrentY()
                        - PdfConstants.SECTION_SPACING
        );

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

                List<List<String>> rows = section.getValue();

                float requiredHeight =
                        rowHeight +                    // category row
                        rowHeight +                    // header row
                        rows.size() * rowHeight +
                        PdfConstants.SECTION_SPACING;

                context = checkPageBreak(
                        document,
                        context,
                        requiredHeight
                );
                // =====================================================
                // CATEGORY ROW (merged cell)
                // =====================================================

                drawCell(
                        context.getContent(),
                        tableX,
                        context.getCurrentY(),
                        availableWidth,
                        rowHeight
                );

                drawCellTextBold(
                        context.getContent(),
                        section.getKey().toUpperCase(),
                        tableX,
                        context.getCurrentY(),
                        availableWidth,
                        rowHeight
                );

                context.setCurrentY(context.getCurrentY() - rowHeight);

                // =====================================================
                // HEADER ROW
                // =====================================================

                float currentX = tableX;

                for (String header : headers) {

                    float width = columnWidths.get(header);

                    drawCell(
                            context.getContent(),
                            currentX,
                            context.getCurrentY(),
                            width,
                            rowHeight
                    );

                    drawCellTextBold(
                            context.getContent(),
                            header,
                            currentX,
                            context.getCurrentY(),
                            width,
                            rowHeight
                    );

                    currentX += width;
                }

                context.setCurrentY(context.getCurrentY() - rowHeight);

                // =====================================================
                // DATA ROWS (MERGED PROPERTY CELLS)
                // =====================================================


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

                    float mergedY = context.getCurrentY() - mergedHeight + rowHeight;

                    drawCell(
                            context.getContent(),
                            propertyX,
                            mergedY,
                            propertyWidth,
                            mergedHeight
                    );

                    drawWrappedCellText(
                            context.getContent(),
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

                        float currentRowY = context.getCurrentY() - (offset * rowHeight);

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
                                    context.getContent(),
                                    currentX,
                                    currentRowY,
                                    width,
                                    rowHeight
                            );

                            drawCellText(
                                    context.getContent(),
                                    row.get(col),
                                    currentX,
                                    currentRowY,
                                    width,
                                    rowHeight
                            );

                            currentX += width;
                        }
                    }

                    context.setCurrentY(
                    context.getCurrentY()
                            - (spanCount * rowHeight)
            );

                    rowIndex += spanCount;
                }

                context.setCurrentY(context.getCurrentY() - PdfConstants.SECTION_SPACING);
            }

                return context;
        }


        private PdfPageContext createNewPage(PDDocument document) throws IOException {

            PDPage page = new PDPage(PDRectangle.A4);

            document.addPage(page);

            PDPageContentStream content =
                    new PDPageContentStream(
                            document,
                            page
                    );

            return new PdfPageContext(
                    page,
                    content,
                    PdfConstants.TOP_Y
            );
        }

        private PdfPageContext checkPageBreak(
                PDDocument document,
                PdfPageContext context,
                float requiredHeight
        ) throws IOException {

            if (
                    context.getCurrentY() - requiredHeight
                            < PdfConstants.BOTTOM_MARGIN
            ) {

                context.getContent().close();

                return createNewPage(document);
            }

            return context;
        }

}
