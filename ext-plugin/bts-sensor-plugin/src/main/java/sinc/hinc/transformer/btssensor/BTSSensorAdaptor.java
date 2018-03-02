package sinc.hinc.transformer.btssensor;

import org.apache.commons.lang3.RandomUtils;
import org.json.simple.JSONObject;
import sinc.hinc.abstraction.ResourceDriver.PluginDataRepository;
import sinc.hinc.abstraction.ResourceDriver.ProviderControlResult;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.transformer.btssensor.model.Sensor;
import sinc.hinc.transformer.btssensor.model.SensorItem;
import sinc.hinc.transformer.btssensor.model.SensorMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

import javax.naming.ldap.Control;
import java.io.IOException;
import java.util.*;

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

    public ProviderControlResult sendControl(ControlPoint controlPoint) {
        ProviderControlResult result = new ProviderControlResult();
        JSONObject configuration = new JSONObject();
        Iterator keyIt = controlPoint.getParameters().keySet().iterator();
        while(keyIt.hasNext()){
            String key = (String) keyIt.next();
            configuration.put(key, controlPoint.getParameters().get(key));
        }
        try {
            String response = APIHandler.post(controlPoint.getReference(), configuration.toJSONString());
            result.setOutput(response);
            result.setResult(ProviderControlResult.CONTROL_RESULT.SUCCESS);
            // TODO create new iot unit, not that urgent local management service periodically scans anyway
            //result.setUpdateIoTUnit();
        } catch (IOException e) {
            result.setOutput(e.getMessage());
            result.setResult(ProviderControlResult.CONTROL_RESULT.COMMAND_EXIT_ERROR);
            logger.error("failed to send control");
            e.printStackTrace();
        }

        return result;
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

    @Override
    public void createResources(PluginDataRepository pluginDataRepository, Map<String, String> settings) {
        Collection<SensorItem> items = this.getItems(settings);
        SensorTransform transformer = new SensorTransform();
        List<IoTUnit> units = new ArrayList<>();
        logger.info("found "+units.size()+"IoTUnits from "+getName());
        for(SensorItem item: items){
            IoTUnit unit = transformer.translateIoTUnit(item);
            units.add(unit);
        }

        pluginDataRepository.saveIoTUnits(units);

        ResourcesProvider provider = this.getProviderAPI(settings);
        List<ResourcesProvider> providers = new ArrayList<>();
        providers.add(provider);
        logger.info("found "+providers.size()+"resource providers from "+getName());
        pluginDataRepository.saveResourceProviders(providers);

        System.out.println("Random number with external dependency: "+RandomUtils.nextInt());

    }
}
