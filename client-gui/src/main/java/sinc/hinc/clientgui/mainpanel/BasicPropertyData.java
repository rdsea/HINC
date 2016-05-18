/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.clientgui.mainpanel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author hungld
 */
public class BasicPropertyData {

    private final StringProperty name;
    private final StringProperty value;

    public BasicPropertyData(String name, String value) {
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleStringProperty(value);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty valueProperty() {
        return value;
    }
}
