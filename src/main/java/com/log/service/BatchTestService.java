package com.log.service;

import com.log.dao.BatchTestDAO;
import com.log.model.Batch;
import com.log.model.BatchTest;

import java.sql.Connection;

public class BatchTestService {

    private BatchTestDAO batchTestDAO;

    public void BatchTest() { this.batchTestDAO = new BatchTestDAO();}

    public int createBatchTest(Connection conn, BatchTest batchTest){
        return batchTestDAO.insertBatchTest(conn, batchTest);
    }
}
