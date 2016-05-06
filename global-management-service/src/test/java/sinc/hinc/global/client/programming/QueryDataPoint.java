/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.client.programming;

import sinc.hinc.global.management.DataPointObservator;
import sinc.hinc.model.PhysicalResource.PhysicalResource;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import sinc.hinc.model.VirtualComputingResource.extensions.SensorProps;
import java.util.List;
import sinc.hinc.common.API.HINCGlobalAPI;
import sinc.hinc.common.API.HINCManagementAPI;
import sinc.hinc.common.metadata.HINCGlobalMeta;
import sinc.hinc.global.API.HINCGlobalAPIImpl;
import sinc.hinc.global.API.HINCManagementImpl;
import sinc.hinc.global.management.QueryManager;

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
        HINCGlobalAPI api = new HINCGlobalAPIImpl();
        HINCManagementAPI mngAPI = new HINCManagementImpl();
        mngAPI.setHINCGlobalMeta(new HINCGlobalMeta("default", "amqp://localhost", "amqp"));
        api.queryDataPoint(3000, null);
        List<DataPoint> datapoints = QueryManager.QueryDataPoints(template);

        // some obmitted code
        for (DataPoint dp : datapoints) {
            DataPointObservator obs = new DataPointObservator(dp) {
                @Override
                public void onChange(DataPoint newVal) {
                    SensorProps props = (SensorProps) newVal.getExtraByType(SensorProps.class);
                    if (props.getRate() > 5) {
                        props.setRate(5);
                    }
                    ControlPoint control = newVal.getControlByName("changeRate");
//                    api.sendControl(control);
                }
            };
        }

    }
}
