/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.abstraction.ResourceDriver.impl;


import at.ac.tuwien.dsg.hinc.abstraction.ResourceDriver.InfoSourceSettings;
import at.ac.tuwien.dsg.hinc.abstraction.ResourceDriver.RawInfoCollector;
import java.util.Map;

/**
 * Raw resource information to get data from REST
 * @author hungld
 */
public class RawInfoCollectorFromREST implements RawInfoCollector {

    @Override
    public Map<String, String> getRawInformation(InfoSourceSettings.InfoSource infoSource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
