package sinc.hinc.logstash.plugin;

import sinc.hinc.abstraction.transformer.IoTUnitTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LogstashTransformer implements IoTUnitTransformer<LogstashItem> {
    @Override
    public IoTUnit translateIoTUnit(LogstashItem data) {
        IoTUnit ioTUnit = new IoTUnit();

        ioTUnit.setResourceID("logstashResourceID");
        DataPoint dataPoint = new DataPoint();
        dataPoint.setName("logstashDataPoint");


        Set<DataPoint> dataPoints = new HashSet<>();

        dataPoints.add(dataPoint);
        ioTUnit.setDatapoints(dataPoints);
        //TODO

        //ioTUnit.
        return ioTUnit;
    }

    @Override
    public String getName() {
        return "logstashPlugin";
    }
}
