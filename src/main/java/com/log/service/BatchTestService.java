package com.log.service;

import com.log.core.BasePropertiesState;
import com.log.dao.BatchDAO;
import com.log.dao.BatchTestDAO;
import com.log.database.DBUtil;
import com.log.model.Batch;
import com.log.model.BatchTest;

import java.sql.Connection;
import java.sql.SQLException;

public class BatchTestService {

    private BatchTestDAO batchTestDAO;
    private BasePropertiesState bpInstance = BasePropertiesState.getInstance();

    public BatchTestService() {
        this.batchTestDAO = new BatchTestDAO();
    }

    public void createBatchTest(Connection conn, BatchTest batchTest){
        int id = getBatchTestId(conn, batchTest);
        if(id != -1){
            System.out.println("Batch Test values already exist in DB");
            bpInstance.setTestId(id);
            return;
        }
         bpInstance.setTestId(batchTestDAO.insertBatchTest(conn, batchTest));
    }

    public int getBatchTestId(Connection conn, BatchTest batchTest){
        return batchTestDAO.getBatchTestId(conn, batchTest);
    }

    public String getBatchIdByTestId(int testId) {

        try(Connection conn = DBUtil.getConnection())
        {
            return batchTestDAO.getBatchIdByTestId(conn, testId);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public BatchTest getBatchTestById(int testId){

        try(Connection conn = DBUtil.getConnection()) {

            return batchTestDAO.getBatchTestById(conn, testId);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
