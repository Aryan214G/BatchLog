package com.log.service.export;

import com.log.database.DBUtil;
import com.log.dto.ReportData;
import com.log.model.Property;
import com.log.service.PropertyService;

import java.sql.SQLException;

public class PdfReportService {

    // ============== IMPORT SERVICES ====================

    private PropertyService propertyService = new PropertyService();

    // ======================================================================


    public ReportData buildReportData(int propertyId) throws SQLException {

        ReportData reportData = new ReportData();
        Property property = propertyService.getPropertyById(propertyId);



        return reportData;
    }
}
