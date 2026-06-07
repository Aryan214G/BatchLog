package com.log.service.export;

import com.log.dto.RetrievalTableReportData;
import com.log.util.pdf.PdfConstants;
import com.log.util.pdf.PdfLayoutUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import static com.log.util.pdf.PdfUtils.writeBoldText;
import static com.log.util.pdf.PdfUtils.writeText;

public class RetrievalTablePdfService
        implements Exporter<RetrievalTableReportData> {

    @Override
    public void export(RetrievalTableReportData data)
            throws Exception {

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream content =
                         new PDPageContentStream(document, page)) {

                 // ========= CONSTANTS =============================================
                float y = PdfConstants.TOP_Y;
                float leftMargin = PdfLayoutUtils.getLeftMargin(page);
                float center = PdfLayoutUtils.getPageCenterX(page);

                 // ==================================================================

                writeBoldText(content, "REPORT", center, y);

                // ========= TOP SECTION =============================================

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
                        "SOP: " + data.getSop(),
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
                        "Test Schedule: " + data.getTestSchedule(),
                        leftMargin,
                        y
                );

                y -= PdfConstants.SECTION_SPACING;
            }
            document.save("retrieval-report.pdf");
        }
    }
}
