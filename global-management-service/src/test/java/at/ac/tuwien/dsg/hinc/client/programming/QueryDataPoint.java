/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.client.programming;

import at.ac.tuwien.dsg.hinc.client.DataPointObservator;
import at.ac.tuwien.dsg.hinc.client.QueryManager;
import at.ac.tuwien.dsg.hinc.model.PhysicalResource.PhysicalResource;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.extensions.SensorProps;
import java.util.List;

/**
 *
 * @author hungld
 */
public class QueryDataPoint {

    public static void main(String[] args) throws Exception {
        DataPoint template = new DataPoint("BodyTemperature");        
        SensorProps sensorProps = new SensorProps();
        sensorProps.setRate(5);

        template.getExtra().add(new PhysicalResource(sensorProps));
        final QueryManager queryMng = new QueryManager("myClient", "ampq://10.0..", "ampq");
        List<DataPoint> datapoints = queryMng.QueryDataPoints(template);

        // some obmitted code
        for (DataPoint dp : datapoints) {
            DataPointObservator obs = new DataPointObservator(dp) {
                @Override
                public void onChange(DataPoint newVal) {
                    SensorProps props = (SensorProps)newVal.getExtraByType(SensorProps.class);
                    if (props.getRate() > 5) {
                        props.setRate(5);
                    }
                    ControlPoint control = newVal.getControlByName("changeRate");
                    queryMng.SendControl(control);
                }
            };
        }

    }
}
