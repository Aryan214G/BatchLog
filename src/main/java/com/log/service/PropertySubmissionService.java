package com.log.service;

import com.log.core.AppState;
import com.log.core.BasePropertiesState;
import com.log.database.DBUtil;
import com.log.model.*;
import com.log.ui.InputRow;
import com.log.util.AlertUtil;
import javafx.scene.control.Alert;

import java.sql.Connection;

public class PropertySubmissionService {


    private TemperatureService temperatureService;
    private DirectionService directionService;
    private CategoryService categoryService;
    private UnitsService unitsService;
    private PropertyService propertyService;
    private PropertyValuesService propertyValuesService;

    private AppState instance = AppState.getInstance();
    private BasePropertiesState bsinstance = BasePropertiesState.getInstance();

    private int tempId;
    private int directionId;
    public PropertySubmissionService(TemperatureService temperatureService,
                                     DirectionService directionService,
                                     CategoryService categoryService,
                                     UnitsService unitsService,
                                     PropertyService propertyService, PropertyValuesService propertyValuesService) {
        this.temperatureService = temperatureService;
        this.directionService = directionService;
        this.categoryService = categoryService;
        this.unitsService = unitsService;
        this.propertyService = propertyService;
        this.propertyValuesService = propertyValuesService;
    }

    public void submit(PropertyState propertyState, String propertyName, String selectedCategory) {
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            handleTemperature(conn, propertyState.getTemperature());
            handleDirection(conn, propertyState.getDirection());
            handleProperty(conn, propertyState, propertyName, selectedCategory, propertyState.getTestMethod());

            conn.commit();

            AlertUtil.showInfo("Submission completed successfully!");

        }  catch (Exception e) {
            AlertUtil.showError("Submit not successful");

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
            System.out.println(
                    "Temperature object = "
                            + temp.getTempVal()
                            + " | "
                            + temp.getTempUnitID()
            );

           this.tempId = temperatureService.insertTemperature(conn, temp);
           temp.setTempId(tempId);

        } else {
            System.out.println(
                    "Temperature object = "
                            + temp.getTempVal()
                            + " | "
                            + temp.getTempUnitID()
            );

            this.tempId = temp.getTempId();
            temperatureService.updateTemperature(conn, temp);
        }
    }

    private void handleDirection(Connection conn, Direction dir) {
        if (dir.getDirId() == null) {
            this.directionId = directionService.getDirectionByName(conn, dir.getDirVal()).getDirId();
            dir.setDirId(directionId);
        }
        else {
            this.directionId = dir.getDirId();
        }
    }

    private void handleProperty(
            Connection conn,
            PropertyState propertyState,
            String propertyName,
            String selectedCategory,
            String testMethod){

        //TODO: might need to replace this unecessary db call
        Category category = categoryService.getCategory(conn, selectedCategory);

        Unit unit =
                unitsService.getUnit(
                        conn,
                        propertyState.getInputRows().get(0)
                                .getUnitController()
                                .getComboBox()
                                .getValue()
                );

        Temperature temperature = new Temperature();
        temperature.setTempId(tempId);

        Direction direction = new Direction();
        direction.setDirId(directionId);

        System.out.println(
                "BasePropertiesState Test ID = " + bsinstance.getTestId());

        // PROPERTY OBJECT

        Property property = propertyState.getProperty();

        if (property == null) {

            property = new Property(
                    propertyName,
                    unit,
                    bsinstance.getTestId(),
                    category,
                    temperature,
                    direction
            );

            if (instance.isEditMode()) {
                propertyState.setProperty(property);
            }
        }

        // TEMPORARY LOGGING
        //TODO: remove this logging later
        System.out.println(
                "Property from state = "
                        + propertyState.getProperty()
        );

        if (propertyState.getProperty() != null) {
            System.out.println(
                    "Property ID = "
                            + propertyState.getProperty().getPropertyID()
            );
        }


        property.setTestMethod(testMethod);

        property.setPropertyName(propertyName);
        property.setUnit(unit);
        property.setCategory(category);
        property.setTemperature(temperature);
        property.setDirection(direction);
        property.setTestMethod(testMethod);
        // ________________________________________________________________________________

        Integer propertyID = property.getPropertyID();

        //TODO: remove this logging
        System.out.println(
                "Property ID before save = "
                        + property.getPropertyID()
        );

        if(propertyID != null && propertyID > 0){
            propertyService.updateProperty(conn, property);
        }
        else{
            propertyID = propertyService.insertProperty(conn, property);
            property.setPropertyID(propertyID);
        }

        for (InputRow row : propertyState.getInputRows()) {

            PropertyValue pv = row.getPropertyValue();

            pv.setPropertyID(propertyID);

            String valueText = row.getField().getText();

            // =====================================
            // EMPTY FIELD → DELETE EXISTING VALUE
            // =====================================

            if (valueText == null || valueText.isBlank()) {

                if (pv.getPropertyValID() != 0
                        && pv.getPropertyValID() != -1) {

                    System.out.println( "Deleting property value");

                    propertyValuesService.deletePropertyValue(
                            conn,
                            pv.getPropertyValID()
                    );
                }

                continue;
            }

            // =====================================
            // VALID NON-EMPTY VALUE
            // =====================================

            try {

                double value =
                        Double.parseDouble(valueText.trim());

                pv.setPropertyVAL(value);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid property value: "
                                + valueText
                );

                continue;
            }

            // =====================================
            // UPDATE
            // =====================================

            if (pv.getPropertyValID() != 0
                    && pv.getPropertyValID() != -1) {

                System.out.println(
                        "Updating property value"
                );

                propertyValuesService.updatePropertyValue(
                        conn,
                        row
                );
            }

            // =====================================
            // INSERT
            // =====================================

            else {

                System.out.println(
                        "Inserting property value"
                );

                int propertyValId = propertyValuesService.insertPropertyValue(
                        conn,
                        row
                );

                row.getPropertyValue().setPropertyValID(propertyValId);
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