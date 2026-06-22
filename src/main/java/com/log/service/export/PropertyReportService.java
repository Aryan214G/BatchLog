package com.log.service.export;

import com.log.dto.ReportData;
import com.log.model.BatchTest;
import com.log.model.Product;
import com.log.model.Property;
import com.log.model.PropertyValue;
import com.log.service.*;
import com.log.util.AlertUtil;
import com.log.util.DateUtils;
import com.log.util.DialogUtils;

import com.log.util.pdf.PdfConstants;
import com.log.util.pdf.PdfLayoutUtils;
import com.log.util.pdf.ReportPreviewDialog;
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
    private ProductService productService = new ProductService();

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

        Product product = productService.getProduct(batchTest.getProductCode());

        List<Double> values = getPropertyValueList(property);

        Double average = null;
        Double stdDev = null;

        if (!values.isEmpty()) {
            average = StatisticsService.mean(values);
            stdDev = StatisticsService.standardDeviation(values);
        }


        String reportNumber = property.getReportNumber();

if (reportNumber == null || reportNumber.isBlank()) {

    AlertUtil.showError(
            "Please enter a Report Number before generating the report."
    );

    return null;
}

        ReportData reportData = new ReportData(
                property.getPropertyName(),
                batchId,
                product.getProductId(),
                product.getProductName(),
                batchTest.getSOP(),
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
                property.getUnit().getUnit(),
                property.getPropertyValues()
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
public void generateReport(
        ReportData reportData
) throws Exception {

    PDDocument document =
            createDocument(reportData);

    ReportPreviewDialog.show(document);
}

    private PDDocument createDocument(ReportData reportData) throws Exception {

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (PDPageContentStream content = new PDPageContentStream(document, page)) {

            float pageWidth = page.getMediaBox().getWidth();
            float leftMargin = 50;
            float rightMargin = pageWidth - 50;
            float y = page.getMediaBox().getHeight() - 50;

            String title = "PROPERTY REPORT";
            float titleX = PdfLayoutUtils.getCenteredTextX(page, title, BOLD_FONT, PdfConstants.FONT_SIZE);

            writeBoldText(content, title, titleX, y);
            y -= PdfConstants.SECTION_SPACING;

            writeText(content, "Report number: " + reportData.getTestReportNo(), leftMargin, y);
            writeRightAlignedText(content, "Date: " + reportData.getDate(), rightMargin, y);
            y -= PdfConstants.SECTION_SPACING;

            writeText(content, "Property: " + reportData.getPropertyName(), leftMargin, y);
            y -= PdfConstants.LINE_SPACING;

            writeText(content, "Batch ID: " + reportData.getBatchId(), leftMargin, y);
            y -= PdfConstants.LINE_SPACING;

            writeText(content, "Product: " + reportData.getProductName() + " (" + reportData.getComponentId() + ")", leftMargin, y);
            y -= PdfConstants.LINE_SPACING;

            if (reportData.getSop() != null) {
                writeText(content, "SOP: " + reportData.getSop(), leftMargin, y);
                y -= PdfConstants.LINE_SPACING;
            }

            writeText(content, "Test Method: " + reportData.getTestMethod(), leftMargin, y);
            y -= PdfConstants.LINE_SPACING;

            writeText(content, "Test Date: " + reportData.getTestDate(), leftMargin, y);
            y -= PdfConstants.LINE_SPACING;

            writeText(content, "Test Site: " + reportData.getTestSite(), leftMargin, y);
            y -= PdfConstants.LINE_SPACING;

            writeText(content, "Direction: " + reportData.getDirection(), leftMargin, y);
            y -= PdfConstants.LINE_SPACING;

            writeText(content, "Temperature: " + reportData.getTemperature() + " " + reportData.getTemperatureUnit(), leftMargin, y);
            y -= PdfConstants.SECTION_SPACING;

            // ── Values ───────────────────────────────────────────────────────────
            writeText(content, "Values: " + reportData.getValues(), leftMargin, y);
            y -= PdfConstants.LINE_SPACING;

            writeText(content, "Unit: " + reportData.getUnit(), leftMargin, y);
            y -= PdfConstants.LINE_SPACING;

            writeText(content, "Min: " + reportData.getMin(), leftMargin, y);
            y -= PdfConstants.LINE_SPACING;

            writeText(content, "Max: " + reportData.getMax(), leftMargin, y);
            y -= PdfConstants.LINE_SPACING;

            writeText(content, "Average: " + reportData.getAverage(), leftMargin, y);
            y -= PdfConstants.LINE_SPACING;

            writeText(
                    content,
                    "Standard deviation: " + reportData.getStandardDeviation(),
                    leftMargin,
                    y
            );
        }

        return document;
    }




}


