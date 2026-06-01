package com.log.service.export;

import com.log.database.DBUtil;
import com.log.dto.ReportData;
import com.log.model.BatchTest;
import com.log.model.Property;
import com.log.service.BatchService;
import com.log.service.BatchTestService;
import com.log.service.PropertyService;

import java.sql.SQLException;

public class PdfReportService {

    // ============== IMPORT SERVICES ====================

    private PropertyService propertyService = new PropertyService();
    private BatchTestService batchTestService = new BatchTestService();
    // ======================================================================


    public ReportData buildReportData(int propertyId) throws SQLException {

        Property property = propertyService.getPropertyById(propertyId);
        String batchId = batchTestService.getBatchIdByTestId(
                property.getTestID()
        );
        BatchTest batchTest = batchTestService.getBatchTestById(
                property.getTestID()
        );

        ReportData reportData = new ReportData(
                property.getPropertyName(),
                batchId,
                property.getTestMethod(),
                batchTest.getTestDate(),
                batchTest.getTestSite(),



        );



        return reportData;
    }
}
