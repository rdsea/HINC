/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.android;

import android.hardware.AndroidSensor;
import java.util.Collection;
import java.util.Map;
import sinc.hinc.abstraction.ResourceDriver.ProviderAdaptor;

/**
 *
 * @author hungld
 */
public class AndroidAPIAdaptor implements ProviderAdaptor<AndroidSensor>{

    @Override
    public Collection<AndroidSensor> getItems(Map<String, String> settings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendControl(String controlAction, Map<String, String> parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getName() {
        return "android";
    }
}
