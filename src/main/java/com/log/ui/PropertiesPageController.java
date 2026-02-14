package com.log.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class PropertiesPageController {

    @FXML
    private ListView<String> propertiesListView;

    private ObservableList<String> properties = FXCollections.observableArrayList(
            "property 1", "property 2", "property 3");

    @FXML
    public void initialize(){
        propertiesListView.setItems(properties);

        //lambda function
        propertiesListView.setCellFactory(listView-> new ListCell<>(){
            private final Button button = new Button();

            //initializer block in place of default constructor
            {
                button.setMaxWidth(Double.MAX_VALUE);
            }

            @Override
            protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);

                if(empty || item == null)
                {
                    setGraphic(null);
                }
                else{
                    button.setText(item);
                    setGraphic(button);
                }
            }
        });
    }
}
