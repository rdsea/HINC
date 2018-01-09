/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.haivancameraprovider.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.communication.processing.HINCMessageSender;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.model.API.WrapperIoTUnit;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

/**
 *
 * @author hungld
 */
public class HaivanCameraProviderAdaptor implements ProviderQueryAdaptor<CameraMetadataItem> {

    static Logger logger = LoggerFactory.getLogger("HaivanCamera");
    private static final String NAME = "HaivanCamera";
    List<CameraMetadataItem> dataItems;
    Map<String, String> meta = null;
    String amqpURL;
    String group;

    public HaivanCameraProviderAdaptor() {
    }

    public HaivanCameraProviderAdaptor(String amqpURL, String group, List<CameraMetadataItem> dataItemsToSend, Map<String, String> meta) {
        this.dataItems = dataItemsToSend;
        this.meta = meta;
        this.amqpURL = amqpURL;
        this.group = group;
    }

    public void push() {
        CameraTransformer transformer = new CameraTransformer();
        WrapperIoTUnit wrapper = new WrapperIoTUnit();
        for (CameraMetadataItem data : dataItems) {
            wrapper.getUnits().add(transformer.translateIoTUnit(data));
        }
        String json = wrapper.toJson();
        logger.debug(json);
        HINCMessageSender comMng = new HINCMessageSender(amqpURL, "amqp");
        HincMessage msg = new HincMessage(HINCMessageType.PROVIDER_UPDATE_IOT_UNIT.toString(), "HaivanCityVideoProvider", HincMessageTopic.getBroadCastTopic(group), "", json);
        msg.hasExtra(group, "experiment");
        msg.getExtra().putAll(meta);
        logger.info(msg.toJson());
        comMng.onewayCall(msg);
        /*
        comMng.asynCall(0, msg, new HINCMessageHander() {
            @Override
            public HincMessage handleMessage(HincMessage message) {
                logger.debug("Provider published message ! But will not recieve any reply. This message will never be shown!");
                return null;
            }
        });
         */
    }

    public void test() {
        HincMessage request = new HincMessage()
                .hasType(HINCMessageType.QUERY_INFORMATION_GLOBAL)
                .hasSenderID("my-application")
                .hasTopic(HincMessageTopic.getBroadCastTopic(group));
        HINCMessageSender sender = new HINCMessageSender(amqpURL, "amqp");
        String responseMsg = sender.synCall(request);
        logger.debug(responseMsg);

    }

    @Override
    public Collection<CameraMetadataItem> getItems(Map<String, String> settings) {
        //clean endpoint a bit
        String endpoint = settings.get("endpoint").trim();
        if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }
        try {
            String dataJson = RestHandler.build(endpoint + "/camera/list", null, null).callGet();
            if (dataJson == null) {
                return null;
            }
            //perform the mapping from json to object
            ObjectMapper mapper = new ObjectMapper();
            CameraIoTDevices data = mapper.readValue(dataJson, CameraIoTDevices.class);
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return NAME;
    }

    //just  for simple test
    public static void main(String args[]) throws InterruptedException {
        if (args.length < 2) {
            System.out.println("Usage: [broker group]");
            System.exit(0);
        }
        CameraIoTDevices allData = new CameraIoTDevices();
        List<CameraMetadataItem> dataItemsToSend = new ArrayList<>();
        CameraMetadataItem item1 = new CameraMetadataItem();
        item1.setId("id1");
        item1.setDescription("camera 1");
        item1.setName("linh");
        item1.setDatapoint("http://2co2.vp9.tv/chn/DNG33");
        dataItemsToSend.add(item1);
        CameraMetadataItem item2 = new CameraMetadataItem();
        item2.setId("id2");
        item2.setDescription("camera 2");
        item2.setName("vu");
        item2.setDatapoint("http://2co2.vp9.tv/chn/DNG2");
        dataItemsToSend.add(item2);

        Map<String, String> meta = new HashMap<>();
        Long st_device_change = new Date().getTime();
        meta.put("st_device_change", st_device_change + "");
        HaivanCameraProviderAdaptor task;
        task = new HaivanCameraProviderAdaptor(args[0], args[1], dataItemsToSend, meta);
        while (true) {
            task.push();
            Thread.sleep(10000);
            task.test();
        }
    }
}
