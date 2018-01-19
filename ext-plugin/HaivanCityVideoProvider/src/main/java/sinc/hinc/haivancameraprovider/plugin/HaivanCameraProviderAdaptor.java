/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.haivancameraprovider.plugin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

/**
 *
 * @author hungld
 */
public class HaivanCameraProviderAdaptor implements ProviderQueryAdaptor<CameraMetadataItem> {

    static Logger logger = LoggerFactory.getLogger("HaivanCamera");
    private static final String NAME = "haivancamera";
    List<CameraMetadataItem> dataItems;
    Map<String, String> meta = null;
    String amqpURL;
    String group;

    public HaivanCameraProviderAdaptor() {
    }
     

    @Override
    public Collection<CameraMetadataItem> getItems(Map<String, String> settings) {
        logger.info("Start getItems");
        //clean endpoint a bit
        String endpoint = settings.get("endpoint").trim();
        logger.info("Start getItems from "+endpoint);
        if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }
        try {
            String dataJson = RestHandler.build(endpoint + "/camera/list", null, null).callGet();
            logger.info(dataJson);
            if (dataJson == null) {
                return null;
            }
            //perform the mapping from json to object
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<CameraMetadataItem> devices = mapper.readValue(dataJson, new TypeReference<List<CameraMetadataItem>>() {
            });
            //annotation with a medata
            for (CameraMetadataItem item:devices) {
                item.getMetadata().put("haivancameraprovider",endpoint);
            }
            CameraIoTDevices data = new CameraIoTDevices();
            data.setDevices(devices); 
            return data.getDevices();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public void sendControl(String controlAction, Map<String, String> parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResourcesProvider getProviderAPI(Map<String, String> settings) { 
        String endpoint = settings.get("endpoint").trim();
        String provider_ref = endpoint;
        ResourcesProvider rp = new ResourcesProvider("haivancamera", endpoint, settings);
        ControlPoint cp = new ControlPoint();
        cp.setName("camera");
        cp.setInvokeProtocol(ControlPoint.InvokeProtocol.POST);
        cp.setControlType(ControlPoint.ControlType.CONNECT_TO_NETWORK);
        cp.belongTo(provider_ref);
        cp.setParameters("cameraname");
        rp.hasApi(cp);
        return rp;
        
    }

    @Override
    public String getName() {
        return NAME;
    }

     
}
