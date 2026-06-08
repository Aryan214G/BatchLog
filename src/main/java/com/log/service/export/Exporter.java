package com.log.service.export;

public interface Exporter<T> {
    void export(T data) throws Exception;
}
