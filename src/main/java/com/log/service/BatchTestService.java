package com.log.service;

import com.log.core.BasePropertiesState;
import com.log.dao.BatchDAO;
import com.log.dao.BatchTestDAO;
import com.log.database.DBUtil;
import com.log.model.Batch;
import com.log.model.BatchTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

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

    public void editTestDate(int testId, String newDate){
        try(Connection conn = DBUtil.getConnection()) {

            batchTestDAO.updateTestDate(conn, testId, newDate);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editTestSite(int testId, String newSite){
        try(Connection conn = DBUtil.getConnection()) {

            batchTestDAO.updateTestSite(conn, testId, newSite);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // BatchTestDAO
    public boolean editSop(int testId, String sop) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "UPDATE Batch_Test SET SOP = ? WHERE Test_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (sop != null) stmt.setString(1, sop);
            else stmt.setNull(1, Types.VARCHAR);
            stmt.setInt(2, testId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean editTestSchedule(int testId, String testSchedule) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "UPDATE Batch_Test SET Test_Schedule = ? WHERE Test_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (testSchedule != null) stmt.setString(1, testSchedule);
            else stmt.setNull(1, Types.VARCHAR);
            stmt.setInt(2, testId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
