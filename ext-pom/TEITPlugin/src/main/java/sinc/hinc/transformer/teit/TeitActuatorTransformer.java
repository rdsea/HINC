/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.teit;

import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import teit.enumactuator.model.EnumActuator;
import teit.enumactuator.model.EnumControl;
import sinc.hinc.abstraction.transformer.PhysicalResourceTransformer;

/**
 *
 * @author hungld
 */
public class TeitActuatorTransformer implements PhysicalResourceTransformer<EnumActuator> {

    @Override
    public IoTUnit translateIoTUnit(EnumActuator actuator) {
        IoTUnit unit = new IoTUnit();
        unit.getPhysicalResourceUuid().add(actuator.getName());
        unit.setState(actuator.getContext());
        unit.hasPhysicalType(actuator.getType());
        for (EnumControl control : actuator.getControls().values()) {
            ControlPoint cp = new ControlPoint(control.getName(), ControlPoint.InvokeProtocol.LOCAL_EXECUTE, actuator.getActuatorJar() + " " + control.getName());
            cp.setConditions(control.getConditions());
            cp.setEffects(control.getEffects());
            unit.hasControlPoint(cp);
        }
        return unit;
    }

    @Override
    public String getName() {
        return "teit-actuator";
    }

}
