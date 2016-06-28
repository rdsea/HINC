/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.clientgui.globalmanager;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author hungld
 */
@Deprecated
public class DataPointTableModel {

    private final StringProperty name;
    private final StringProperty resourceID;
    private final StringProperty description;
    // for data point
    private final StringProperty measurementUnit;

    public DataPointTableModel(String name, String resourceID, String description, String measurementUnit) {
        this.name = new SimpleStringProperty(name);
        this.resourceID = new SimpleStringProperty(resourceID);
        this.description = new SimpleStringProperty(description);
        this.measurementUnit = new SimpleStringProperty(measurementUnit);

    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty resourceIDProperty() {
        return resourceID;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty measurementUnitProperty() {
        return measurementUnit;
    }

}
