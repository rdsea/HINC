/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.ResourceDriver.impl;


import sinc.hinc.abstraction.ResourceDriver.InfoSourceSettings;
import java.util.Map;
import sinc.hinc.abstraction.ResourceDriver.ProviderAdaptor;

/**
 * Raw resource information to get data from REST
 * @author hungld
 */
public class RawInfoCollectorFromREST implements ProviderAdaptor {

    @Override
    public Map<String, String> getRawInformation(InfoSourceSettings.InfoSource infoSource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
