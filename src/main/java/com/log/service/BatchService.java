package com.log.service;

import com.log.dao.BatchDAO;
import com.log.model.Batch;

import java.util.List;

public class BatchService {

    private BatchDAO batchDAO;

    public BatchService() {
        this.batchDAO = new BatchDAO();
    }

    public void createBatch(Batch batch) {

        if (batch == null) {
            throw new IllegalArgumentException("Batch cannot be null");
        }

        batchDAO.insertBatch(batch);
    }

    public Batch getBatchByCode(int batchCode) {

        if (batchCode <= 0) {
            throw new IllegalArgumentException("Invalid batch code");
        }

        return batchDAO.getBatch(batchCode);
    }

    public List<Batch> getAllBatches() {
        return batchDAO.getAllBatches();
    }

    public void deleteBatch(int batchCode) {

        if (batchCode <= 0) {
            throw new IllegalArgumentException("Invalid batch code");
        }

        batchDAO.deleteBatch(batchCode);
    }
}