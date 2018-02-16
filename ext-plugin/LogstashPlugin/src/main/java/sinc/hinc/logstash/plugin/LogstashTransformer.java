package sinc.hinc.logstash.plugin;

import sinc.hinc.abstraction.transformer.IoTUnitTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

import java.util.HashSet;
import java.util.Set;

public class LogstashTransformer implements IoTUnitTransformer<LogstashItem> {
    @Override
    public IoTUnit translateIoTUnit(LogstashItem data) {
        IoTUnit ioTUnit = new IoTUnit();

        ioTUnit.setResourceID("logstashResourceID");
        DataPoint dataPoint = new DataPoint();
        dataPoint.setName("logstashDataPoint");


        Set<ControlPoint> controlPoints = new HashSet<>();

        ControlPoint start = new ControlPoint();

        start.setName("start");
        start.setInvokeProtocol(ControlPoint.InvokeProtocol.LOCAL_ASYNC_EXECUTE);
        start.setControlType(ControlPoint.ControlType.EXECUTE_LOCAL_PROCESS);

        start.setReference("logstash");
        //"-e input {generator{count => 25}} output { stdout {} }"
        start.setParameters("");

        //Parameter for pipeline http://localhost:80 -> amqp://localhost:5672
        //-e input { http_poller{ urls => { datasource => { url => "http://localhost:80" method => get} } schedule => {"every" => "5s"} } } output { rabbitmq{ host => "localhost" exchange_type => "direct" exchange => "push_rabbitmq" } }

        controlPoints.add(start);



        ControlPoint stop = new ControlPoint();
        stop.setName("stop");
        stop.setInvokeProtocol(ControlPoint.InvokeProtocol.LOCAL_ASYNC_EXECUTE);
        stop.setControlType(ControlPoint.ControlType.DESTROY_LOCAL_PROCESS);
        stop.setReference("logstash");
        controlPoints.add(stop);



        ioTUnit.setControlpoints(controlPoints);

        return ioTUnit;
    }

    @Override
    public String getName() {
        return "logstashPlugin";
    }
}
