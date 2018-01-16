/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.testrigprovider.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import sinc.hinc.abstraction.transformer.IoTUnitTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

/**
 *
 * @author linhsolar
 */
public class TestRigProviderTransformer implements IoTUnitTransformer<TestRigMetadataItem> {

    @Override
    public IoTUnit translateIoTUnit(TestRigMetadataItem data) {
        //we have to check the data carefully as the metadata describes 
        //different types of devices
        //here we just few
        IoTUnit unit = new IoTUnit();
        unit.setHincID(UUID.randomUUID().toString());
        unit.setResourceID(data.getId());
        if (data.getDistance() !=null) {
            DataPoint dp = new DataPoint();
            dp.setName("distance");
            dp.setDataApi(data.getDistance().get("url"));
            unit.hasDatapoint(dp);    
        }
        if (data.getHeartrate() !=null) {
            DataPoint heartrate_dp = new DataPoint();
            heartrate_dp.setName("heartrate");
            heartrate_dp.setDataApi(data.getHeartrate().get("url"));
            unit.hasDatapoint(heartrate_dp);    
        }  
  if (data.getPosition() !=null) {
            DataPoint position_dp = new DataPoint();
            position_dp.setName("position");
            position_dp.setDataApi(data.getPosition().get("url"));
            unit.hasDatapoint(position_dp);    
        }
  if (data.getVoltage()!=null) {
            DataPoint voltage_dp = new DataPoint();
            voltage_dp.setName("voltage");
            voltage_dp.setDataApi(data.getVoltage().get("url"));
            unit.hasDatapoint(voltage_dp);    
        }
  if (data.getMove()!=null) {
            ControlPoint move_cp = new ControlPoint();
            move_cp.setName("move");
            move_cp.setReference(data.getMove().get("url"));
            move_cp.setInvokeProtocol(ControlPoint.InvokeProtocol.POST);
            move_cp.setControlType(ControlPoint.ControlType.CONNECT_TO_NETWORK);
            unit.hasControlPoint(move_cp);    
        }
        return unit;
    }

    @Override
    public String getName() {
        return "testrig";
    }

}
