/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.VirtualComputingResource;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author hungld
 */
//linhsolar: represent a Gateway
public class SoftwareDefinedGateway {

    Set<IoTUnit> units = new HashSet<>();

    public Set<IoTUnit> getUnits() {
        return units;
    }

    public void setUnits(Set<IoTUnit> units) {
        this.units = units;
    }

}
