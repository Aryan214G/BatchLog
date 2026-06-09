package com.log.service.export;

import com.log.dto.ReportData;
import com.log.model.BatchTest;
import com.log.model.Property;
import com.log.model.PropertyValue;
import com.log.service.*;
import com.log.util.DateUtils;
import com.log.util.DialogUtils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.printing.PDFPageable;

import java.awt.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.log.util.pdf.PdfUtils.*;

public class PropertyReportService
        implements ReportGenerator<ReportData> {

    // ============== IMPORT SERVICES ====================

    private PropertyService propertyService = new PropertyService();
    private BatchTestService batchTestService = new BatchTestService();
    private PropertyValuesService propertyValuesService = new PropertyValuesService();

    // ============== IMPORT UTILITY CLASSES ====================


    // ======================================================================

    public ReportData buildReportData(int propertyId) throws SQLException {

        Property property = propertyService.getPropertyById(propertyId);
        property.setPropertyValues(
                propertyValuesService.getValuesByProperty(propertyId)
        );

        String batchId = batchTestService.getBatchIdByTestId(
                property.getTestID()
        );

        BatchTest batchTest = batchTestService.getBatchTestById(
                property.getTestID()
        );

        List<Double> values = getPropertyValueList(property);

        Double average = null;
        Double stdDev = null;

        if (!values.isEmpty()) {
            average = StatisticsService.mean(values);
            stdDev = StatisticsService.standardDeviation(values);
        }


        Optional<String> result = DialogUtils.showTextInputDialog(
        "Report Number",
        "Generate Report",
        "Enter report number:"
    );
        String reportNumber = result.orElse(null);

        ReportData reportData = new ReportData(
                property.getPropertyName(),
                batchId,
                property.getTestMethod(),
                batchTest.getTestDate(),
                batchTest.getTestSite(),
                property.getDirection().getDirVal(),
                property.getTemperature().getTempVal(),
                property.getTemperature().getTempUnitVal(),
                reportNumber,
                DateUtils.getCurrentDateFormatted(),
                findMin(property),
                findMax(property),
                average,
                stdDev,
                values,
                property.getUnit().getUnit()
        );



        return reportData;
    }

    private List<Double> getPropertyValueList(Property property) {
    return property.getPropertyValues()
            .stream()
            .map(PropertyValue::getPropertyVAL)
            .filter(Objects::nonNull)
            .toList();
}

    private Double findMax(Property property){

        return property.getPropertyValues()
            .stream()
            .map(PropertyValue::getPropertyVAL)
            .filter(Objects::nonNull)
            .max(Double::compare)
            .orElse(null);
    }

    private Double findMin(Property property){

        return property.getPropertyValues()
            .stream()
            .map(PropertyValue::getPropertyVAL)
            .filter(Objects::nonNull)
            .min(Double::compare)
            .orElse(null);
    }

     // ======================================================================

    @Override
    public void generateReport(ReportData reportData) throws IOException, PrinterException {

//        FileChooser chooser = new FileChooser();
//
//        chooser.setTitle("Save PDF Report");
//
//        chooser.getExtensionFilters().add(
//                new FileChooser.ExtensionFilter(
//                        "PDF Files",
//                        "*.pdf"
//                )
//        );
//
//        chooser.setInitialFileName(
//                reportData.getPropertyName() + "_Report.pdf"
//        );
//
//        File file = chooser.showSaveDialog(ownerWindow);
//
//        if (file == null) {
//            return null; // user pressed cancel
//        }

    try (PDDocument document = new PDDocument()) {

        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (PDPageContentStream content =
                     new PDPageContentStream(document, page)) {

            // CONSTANTS

            float pageWidth = page.getMediaBox().getWidth();

            float LEFT_MARGIN = pageWidth * 0.08f;
            float RIGHT_MARGIN = pageWidth * 0.92f;

            float RIGHT_COLUMN_X = pageWidth - 200;

            float y = 750;
            // ________________________________________________________________________________


            String title = "TEST REPORT - " + reportData.getPropertyName().toUpperCase();

            PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            float fontSize = 12;

            float titleWidth = font.getStringWidth(title) / 1000 * fontSize;
            float titleX = (page.getMediaBox().getWidth() - titleWidth) / 2;

            writeBoldText(content, title, titleX, y);

            // ________________________________________________________________________________

            y -= 40;

            float RIGHT_EDGE = 550;

            writeText(content, "Report Number: " + reportData.getTestReportNo(), 50, y);

            writeRightAlignedText(content, "Date: " + reportData.getDate(), RIGHT_EDGE, y);

            // ________________________________________________________________________________

            y -= 40;

            writeText(content, "Batch ID: " + reportData.getBatchId(), 50, y);
            writeRightAlignedText(content, "Test Method: " + reportData.getTestMethod(), RIGHT_EDGE, y);

            y -= 20;

            writeText(content, "Test Site: " + reportData.getTestSite(), 50, y);
            writeRightAlignedText(content, "Test temperature: " + reportData.getTemperature(), RIGHT_EDGE, y);

            y -= 20;

            writeText(content, "Test Date: " + reportData.getTestDate(), 50, y);
            writeRightAlignedText(content, "Direction: " + reportData.getDirection(), RIGHT_EDGE, y);

            // ________________________________________________________________________________

            y -= 30;

            drawLine(content, LEFT_MARGIN, y, RIGHT_MARGIN, y);

            // ________________________________________________________________________________

            y -= 30;

            String propertyName = reportData.getPropertyName().toUpperCase();
            String unit = reportData.getUnit();

            float propertyWidth = font.getStringWidth(propertyName) / 1000 * fontSize;
            float propertyX = (page.getMediaBox().getWidth() - propertyWidth) / 2;

            writeBoldText(content, propertyName  + " (" + unit + ")", propertyX, y);

            y -= 60;

            //table constrains

            float tableX = LEFT_MARGIN + 150;
            float tableY = y;
            float rowHeight = 25;

            float col1 = 100;
            float col2 = 100;

            drawCell(content, tableX, tableY, col1, rowHeight);
            drawCell(content, tableX + col1, tableY, col2, rowHeight);

            drawCellTextBold(content, "Specimen No.", tableX, tableY, col1, rowHeight);
            drawCellTextBold(content, "Test value", tableX + col2, tableY, col2, rowHeight);

            int sNo = 1;
            for(Double value : reportData.getValues())
            {
                y-= 25;

                drawCell(content, tableX, y, col1, rowHeight);
                drawCell(content, tableX + col1, y, col2, rowHeight);

                drawCellText(content, "Specimen " + String.valueOf(sNo), tableX, y, col1, rowHeight);
                drawCellText(content, value.toString(), tableX + col1, y, col2, rowHeight);

                sNo++;
            }

            y -= 30;

            drawLine(content, LEFT_MARGIN, y, RIGHT_MARGIN, y);

            // ________________________________________________________________________________

            y -= 30;

            writeBoldText(content, "STATISTICS", 250, y);

            y-= 20;

            writeText(content, "Minimum: " + String.format("%.1f", reportData.getMin()), 50, y);

            y-= 20;

            writeText(content, "Maximum: " + String.format("%.1f", reportData.getMax()), 50, y);

            y-= 20;

            writeText(content, "Average: " + String.format("%.1f", reportData.getAverage()), 50, y);

            y-= 20;

            String standardDeviation =
        reportData.getStandardDeviation() == null
                ? "-"
                : String.format(
                        "%.1f",
                        reportData.getStandardDeviation()
                );

            writeText(content, "Standard deviation: " + standardDeviation, 50, y);

        }

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


}

}
