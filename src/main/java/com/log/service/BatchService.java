package com.log.service;

import com.log.core.BasePropertiesState;
import com.log.dao.BatchDAO;
import com.log.model.Batch;

import java.sql.Connection;
import java.util.List;

public class BatchService {

    private BatchDAO batchDAO;
    private BasePropertiesState bpinstance = BasePropertiesState.getInstance();

    public BatchService() {
        this.batchDAO = new BatchDAO();
    }

    public void createBatch(Connection conn, Batch batch) {

        if (batch == null) {
            throw new IllegalArgumentException("Batch cannot be null");
        }
        int batchCode = getBatchCode(conn, batch);
        if(batchCode != -1){
            System.out.println("Batch already exists in DB");
            bpinstance.setBatchCode(batchCode);
            return;
        }
        bpinstance.setBatchCode(batchDAO.insertBatchByID(conn, batch));
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

    public int getBatchCode(Connection conn, Batch batch){
        return batchDAO.getBatchCode(conn, batch);
    }
}