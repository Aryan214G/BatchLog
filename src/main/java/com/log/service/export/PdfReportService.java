package com.log.service.export;

import com.log.dto.ReportData;
import com.log.service.PropertyService;

public class PdfReportService {

    // ============== IMPORT SERVICES ====================

    private PropertyService propertyService = new PropertyService();

    // ======================================================================


    public ReportData buildReportData(int propertyId) {

        ReportData reportData = new ReportData();

        propertyService.getPropertyB


        return reportData;
    }
}
