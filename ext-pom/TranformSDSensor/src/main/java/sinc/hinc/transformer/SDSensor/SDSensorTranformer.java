/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.SDSensor;


import java.util.ArrayList;
import java.util.List;
import sinc.hinc.abstraction.transformer.IoTResourceTransformation;
import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ExecutionEnvironment;

/**
 * The rawData is the content of salsa.meta file, which defines the list of capability
 *
 * @author hungld
 */
public class SDSensorTranformer implements IoTResourceTransformation<SDSensorMeta> {

    @Override
    public DataPoint updateDataPoint(SDSensorMeta data) {
        DataPoint dp = new DataPoint(data.getName(), "DataPoint_" + data.getName(), "SD Sensor", data.getType(), "N/A");
        return dp;
    }

    @Override
    public List<ControlPoint> updateControlPoint(SDSensorMeta data) {
        List<ControlPoint> cps = new ArrayList<>();
        for (String key : data.getActions().keySet()) {
            ControlPoint cp = new ControlPoint(data.getName(), key, "Action " + key, ControlPoint.InvokeProtocol.LOCAL_EXECUTE, data.getActions().get(key), "");
            cps.add(cp);
        }
        return cps;
    }

    @Override
    public ExecutionEnvironment updateExecutionEnvironment(SDSensorMeta data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CloudConnectivity updateCloudConnectivity(SDSensorMeta data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
