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

/*
 * linhsolar: represent a Software-defined Gateway (SDG)
 * SDG is an abstract entity and we use it to manage different IoT Units
 * that have something in common. For example, SDG could be a virtual gateway
 * provided for a customer with similar execution policies and underlying resource
 * capabilities (e.g., CPU, RAM). 
 * SDG can also be used to represent edge systems.  
 */
public class SoftwareDefinedGateway {

    Set<IoTUnit> units = new HashSet<>();

    public Set<IoTUnit> getUnits() {
        return units;
    }

    public void setUnits(Set<IoTUnit> units) {
        this.units = units;
    }

}
