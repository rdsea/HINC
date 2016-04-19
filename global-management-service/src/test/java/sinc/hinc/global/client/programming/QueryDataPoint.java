/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.client.programming;

import sinc.hinc.global.client.DataPointObservator;
import sinc.hinc.global.client.QueryManager;
import sinc.hinc.model.PhysicalResource.PhysicalResource;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import sinc.hinc.model.VirtualComputingResource.extensions.SensorProps;
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
