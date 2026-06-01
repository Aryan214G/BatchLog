package com.log.service.export;

import com.log.database.DBUtil;
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
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PdfReportService {

    // ============== IMPORT SERVICES ====================

    private PropertyService propertyService = new PropertyService();
    private BatchTestService batchTestService = new BatchTestService();
    private PropertyValuesService propertyValuesService = new PropertyValuesService();

    // ============== IMPORT UTILITY CLASSES ====================

    private DateUtils dateUtils = new DateUtils();

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
                dateUtils.getCurrentDateFormatted(),
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

    public File generatePdf(ReportData reportData) throws IOException {

    File file = new File("report.pdf");

    try (PDDocument document = new PDDocument()) {

        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream content =
                     new PDPageContentStream(document, page)) {

            // CONSTANTS

            float pageWidth = page.getMediaBox().getWidth();

            float LEFT_MARGIN = pageWidth * 0.08f;
            float RIGHT_MARGIN = pageWidth * 0.92f;

            float RIGHT_COLUMN_X = pageWidth - 200;

            float y = 700;
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

            writeBoldText(content, propertyName  + " (" + unit + ")", 250, y);

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

            writeText(content, "Minimum: " + reportData.getMin(), 50, y);

            y-= 20;

            writeText(content, "Maximum: " + reportData.getMax(), 50, y);

            y-= 20;

            writeText(content, "Average: " + reportData.getAverage(), 50, y);

            y-= 20;

            String standardDeviation = String.format("%.1f", reportData.getStandardDeviation());

            writeText(content, "Standard deviation: " + standardDeviation, 50, y);

        }

        document.save(file);
    }

    return file;
}

    private void writeText(
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

    private void writeBoldText(
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

    private void writeRightAlignedText(PDPageContentStream content, String text, float rightX, float y) throws IOException {

        PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        float fontSize = 12;

        float textWidth = font.getStringWidth(text) / 1000 * fontSize;

        writeText(content, text, rightX - textWidth, y);
    }

    private void drawLine(PDPageContentStream content, float startX, float startY, float endX, float endY) throws IOException {

    content.moveTo(startX, startY);
    content.lineTo(endX, endY);
    content.stroke();
}

    // ======================== TABLES ====================================

    private void drawCell(PDPageContentStream content, float x, float y, float width, float height) throws IOException {

        content.addRect(x, y, width, height);
        content.stroke();
    }

    private void drawCellText(PDPageContentStream content, String text, float x, float y, float cellWidth, float cellHeight) throws IOException {

    PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    float fontSize = 12;

    float textWidth = font.getStringWidth(text) / 1000 * fontSize;

    float textX = x + (cellWidth - textWidth) / 2;
    float textY = y + (cellHeight - fontSize) / 2 + 4;

    writeText(content, text, textX, textY);
}

private void drawCellTextBold(PDPageContentStream content, String text, float x, float y, float cellWidth, float cellHeight) throws IOException {

    PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    float fontSize = 12;

    float textWidth = font.getStringWidth(text) / 1000 * fontSize;

    float textX = x + (cellWidth - textWidth) / 2;
    float textY = y + (cellHeight - fontSize) / 2 + 4;

    writeBoldText(content, text, textX, textY);
}


}
