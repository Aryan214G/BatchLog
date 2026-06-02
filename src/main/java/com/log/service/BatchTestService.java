package com.log.service;

import com.log.core.BasePropertiesState;
import com.log.dao.BatchTestDAO;
import com.log.model.BatchTest;

import java.sql.Connection;

public class BatchTestService {

    private final BatchTestDAO batchTestDAO;
    private final BasePropertiesState bpInstance =
            BasePropertiesState.getInstance();

    public BatchTestService() {
        this.batchTestDAO = new BatchTestDAO();
    }

    public void createBatchTest(
            Connection conn,
            BatchTest batchTest
    ) {

        System.out.println();
        System.out.println(
                "======================================"
        );
        System.out.println(
                "CREATE BATCH TEST CALLED"
        );
        System.out.println(
                "Batch Code = "
                        + batchTest.getBatchCode()
        );
        System.out.println(
                "Test Date = "
                        + batchTest.getTestDate()
        );
        System.out.println(
                "Test Site = "
                        + batchTest.getTestSite()
        );

        int id = getBatchTestId(
                conn,
                batchTest
        );

        System.out.println(
                "getBatchTestId() returned = "
                        + id
        );

        if (id != -1) {

            System.out.println(
                    "Batch Test already exists."
            );

            bpInstance.setTestId(id);

            System.out.println(
                    "BasePropertiesState Test ID set to = "
                            + bpInstance.getTestId()
            );

            System.out.println(
                    "======================================"
            );

            return;
        }

        int newId =
                batchTestDAO.insertBatchTest(
                        conn,
                        batchTest
                );

        System.out.println(
                "insertBatchTest() returned = "
                        + newId
        );

        bpInstance.setTestId(newId);

        System.out.println(
                "BasePropertiesState Test ID set to = "
                        + bpInstance.getTestId()
        );

        System.out.println(
                "======================================"
        );
    }

    public int getBatchTestId(
            Connection conn,
            BatchTest batchTest
    ) {

        int id =
                batchTestDAO.getBatchTestId(
                        conn,
                        batchTest
                );

        System.out.println(
                "BatchTestDAO.getBatchTestId() = "
                        + id
        );

        return id;
    }
}