package com.log.service.export;

public interface ReportGenerator<T> {
    void generateReport(T data) throws Exception;
}
