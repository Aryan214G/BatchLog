package com.log.service;

import com.log.core.BasePropertiesState;
import com.log.database.DBUtil;
import com.log.model.*;
import com.log.ui.InputRow;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.util.List;

public class PropertySubmissionService {

    private TemperatureService temperatureService;
    private DirectionService directionService;
    private CategoryService categoryService;
    private UnitsService unitsService;
    private PropertyService propertyService;
    private BasePropertiesState bsinstance = BasePropertiesState.getInstance();

    private int tempId;
    private int directionId;
    public PropertySubmissionService(TemperatureService temperatureService,
                                     DirectionService directionService,
                                     CategoryService categoryService,
                                     UnitsService unitsService,
                                     PropertyService propertyService) {
        this.temperatureService = temperatureService;
        this.directionService = directionService;
        this.categoryService = categoryService;
        this.unitsService = unitsService;
        this.propertyService = propertyService;
    }

    public void submit(PropertyState propertyState, String propertyName, String selectedCategory) {
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            handleTemperature(conn, propertyState.getTemperature());
            handleDirection(conn, propertyState.getDirection());
            handleProperty(conn, propertyState.getInputRows(), propertyName, selectedCategory);

            conn.commit();
            showAlert("Success", "Submission completed successfully!");
        }  catch (Exception e) {
            showAlert("Submit not successful",e.toString());

            e.printStackTrace();

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void handleTemperature(Connection conn, Temperature temp) {
        if (temp.getTempId() == null) {
           this.tempId = temperatureService.createTemperature(conn, temp);
        } else {
            temperatureService.updateTemperature(conn, temp);
        }
    }

    private void handleDirection(Connection conn, Direction dir) {
        if (dir.getDirId() == null) {
            this.directionId = directionService.getDirectionByName(conn, dir.getDirVal()).getDirId();
        }
    }

    private void handleProperty(Connection conn, List<InputRow> inputRows, String propertyName, String selectedCategory){
        Property property = new Property(
                propertyName,
                categoryService.getCategory(conn, selectedCategory).getCategoryId(),
                tempId,
                directionId,
                unitsService.getUnit(conn, inputRows.get(0).getUnitController().getComboBox().getValue()).getUnitId(),
                bsinstance.getBatchCode()
        );

        int propertyID = propertyService.insertProperty(conn, property);
        if(propertyID != -1) {
            System.out.println("Updating Property");
            property.setPropertyID(propertyID);
            propertyService.updateProperty(conn, property);
        }

        for (InputRow row : inputRows){

            int propertyValID = propertyService.getPropertyValueId(conn, propertyID);
            row.getPropertyValue().setPropertyValID(propertyValID);
            row.getPropertyValue().setPropertyID(propertyID);

            //check if propertyValue exists in DB
            if(row.getPropertyValue().getPropertyValID() != -1
                    || propertyValID != -1){
                //exists
                System.out.println("Property value already exists, updating value");
                propertyService.updatePropertyValue(conn, row);
            }
            else {
                propertyService.insertPropertyValue(conn, row);
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}