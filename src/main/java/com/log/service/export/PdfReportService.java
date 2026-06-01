package com.log.service.export;

import com.log.database.DBUtil;
import com.log.dto.ReportData;
import com.log.model.BatchTest;
import com.log.model.Property;
import com.log.model.PropertyValue;
import com.log.service.BatchService;
import com.log.service.BatchTestService;
import com.log.service.PropertyService;
import com.log.service.StatisticsService;
import com.log.util.DateUtils;
import com.log.util.DialogUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PdfReportService {

    // ============== IMPORT SERVICES ====================

    private PropertyService propertyService = new PropertyService();
    private BatchTestService batchTestService = new BatchTestService();


    // ============== IMPORT UTILITY CLASSES ====================

    private DateUtils dateUtils = new DateUtils();

    // ======================================================================

    public ReportData buildReportData(int propertyId) throws SQLException {

        Property property = propertyService.getPropertyById(propertyId);
        String batchId = batchTestService.getBatchIdByTestId(
                property.getTestID()
        );
        BatchTest batchTest = batchTestService.getBatchTestById(
                property.getTestID()
        );

        List<Double> values = getPropertyValueList(property);

        double average = StatisticsService.mean(values);
        double stdDev = StatisticsService.standardDeviation(values);


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
}
