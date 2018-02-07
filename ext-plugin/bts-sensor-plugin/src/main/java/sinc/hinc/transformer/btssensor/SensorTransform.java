package sinc.hinc.transformer.btssensor;

import sinc.hinc.transformer.btssensor.model.SensorItem;
import sinc.hinc.abstraction.transformer.IoTUnitTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;

public class SensorTransform  implements IoTUnitTransformer<SensorItem>{
    public IoTUnit translateIoTUnit(SensorItem sensorItem) {
        IoTUnit unit = new IoTUnit();
        unit.setResourceID(sensorItem.getSensor().getId());

        DataPoint dp = new DataPoint();
        dp.setDatatype(sensorItem.getMetadata().getMeasurement());
        dp.setConnectingTo(new AccessPoint(sensorItem.getSensor().getBroker()));
        dp.setMeasurementUnit(sensorItem.getMetadata().getUnit());
        dp.setName(sensorItem.getMetadata().getUnit());

        unit.hasDatapoint(dp);
        return unit;
    }

    public String getName() {
        return "bts-sensor";
    }
}
