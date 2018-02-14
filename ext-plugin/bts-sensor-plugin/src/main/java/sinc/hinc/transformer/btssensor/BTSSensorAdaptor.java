package sinc.hinc.transformer.btssensor;

import sinc.hinc.transformer.btssensor.model.Sensor;
import sinc.hinc.transformer.btssensor.model.SensorItem;
import sinc.hinc.transformer.btssensor.model.SensorMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BTSSensorAdaptor implements ProviderQueryAdaptor<SensorItem>{
    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public Collection<SensorItem> getItems(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();

        List<SensorItem> sensorItems = new ArrayList<SensorItem>();

        // first we get the metadata
        List<SensorMetadata> sensorMetadata = new ArrayList<SensorMetadata>();

        try {
            logger.info("fetching sensor metadata");
            sensorMetadata = SensorMetadata.getMetaData(APIHandler.get(endpoint,"/sensor/bts"));
            logger.info("found "+sensorMetadata.size()+" metadata entities");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // go through each sensor endpoint and extract sensors
        logger.info("fetching sensors");
        for(SensorMetadata metadata: sensorMetadata) {
            List<Sensor> sensors = new ArrayList<Sensor>();
            try {
                sensors = Sensor.getSensors(APIHandler.get(endpoint, metadata.getUrl()));
                logger.info(sensors.size() + " " + metadata.getType() + " sensors found");
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Sensor sensor : sensors) {
                SensorItem item = new SensorItem();
                item.setMetadata(metadata);
                item.setSensor(sensor);
                sensorItems.add(item);
            }
        }

        return sensorItems;
    }

    public void sendControl(String s, Map<String, String> settings) {

    }

    public ResourcesProvider getProviderAPI(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();
        ResourcesProvider provider = new ResourcesProvider();
        provider.setSettings(settings);
        provider.setName("bts sensor provider");
        provider.setUri(endpoint+"/sensor/bts");

        List<ControlPoint> controlPoints = new ArrayList<ControlPoint>();

        // we get the metadata
        logger.info("obtaining sensor metadata");
        List<SensorMetadata> sensorMetadata = new ArrayList<SensorMetadata>();

        try {
            sensorMetadata = SensorMetadata.getMetaData(APIHandler.get(endpoint,"/sensor/bts"));
            logger.info(sensorMetadata.size()+" metadata obtained");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // go through each sensor type and grab it's configuration
        for(SensorMetadata metadata: sensorMetadata){
            ControlPoint controlPoint = new ControlPoint();
            controlPoint.setControlType(ControlPoint.ControlType.PROVISION);
            controlPoint.setName(metadata.getType() + " sensor provider");
            controlPoint.setInvokeProtocol(ControlPoint.InvokeProtocol.POST);

            controlPoint.setReference(metadata.getUrl());
            controlPoint.setParameters(metadata.getConfiguration());
            controlPoint.setIotUnitID(provider.getUri());
            controlPoints.add(controlPoint);
        }
        logger.info(controlPoints.size()+" apis obtained from "+provider.getName());
        provider.setApis(controlPoints);

        return provider;
    }

    public String getName() {
        return "bts-sensor";
    }
}
