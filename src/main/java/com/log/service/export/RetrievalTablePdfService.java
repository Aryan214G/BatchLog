package com.log.service.export;

import com.log.dto.RetrievalTableReportData;
import com.log.util.pdf.PdfConstants;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import static com.log.util.pdf.PdfUtils.writeBoldText;

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

                float y = PdfConstants.TOP_Y;

                writeBoldText(
                        content,
                        "REPORT",
                        250,
                        y
                );
            }
            document.save("retrieval-report.pdf");
        }
    }
}
