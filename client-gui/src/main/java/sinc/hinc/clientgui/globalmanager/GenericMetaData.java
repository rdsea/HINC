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
public class GenericMetaData {
    private final StringProperty key;
    private final StringProperty value;

    public GenericMetaData(String key, String value) {
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
    }
    
     public StringProperty keyProperty() {
        return key;
    }
      public StringProperty valueProperty() {
        return value;
    }
}
