package com.log.service.export;

import com.log.dto.RetrievalTableReportData;
import com.log.util.DateUtils;
import com.log.util.DialogUtils;
import com.log.util.pdf.PdfConstants;
import com.log.util.pdf.PdfLayoutUtils;
import com.log.util.pdf.PdfUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

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

                 // ==================================================================


                writeBoldText(content, title, titleX, y);

                // ========= TOP SECTION =============================================

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
            }
            document.save("retrieval-report.pdf");
        }
    }
}
