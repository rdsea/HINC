/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.haivancameraprovider.provider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HINCMessageSender;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.model.API.WrapperIoTUnit;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;

/**
 *
 * @author hungld
 */
public class CameraInfoTaskPushHINCLocal {

    static Logger logger = LoggerFactory.getLogger("HaivanCamera");
    List<CameraMetadataItem> dataItems;
    Map<String, String> meta = null;
    String amqpURL;
    String group ;
    public CameraInfoTaskPushHINCLocal(String amqpURL, String group, List<CameraMetadataItem> dataItemsToSend, Map<String, String> meta) {
        this.dataItems = dataItemsToSend;
        this.meta = meta;
        this.amqpURL = amqpURL;
        this.group =group;
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
        comMng.synCall(msg);
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
                .hasTopic(HincMessageTopic.getBroadCastTopic("haivancamera"));
        HINCMessageSender sender = new HINCMessageSender(amqpURL, "amqp");
        String responseMsg = sender.synCall(request);
        logger.debug(responseMsg);

    }

    public static void main(String args[]) throws InterruptedException {
        if (args.length < 3) {
            System.out.println("Usage: " +args[0] + "[broker group]");
            System.exit(0);
        }
        CameraIoTDevices allData = new CameraIoTDevices();
        List<CameraMetadataItem> dataItemsToSend = new ArrayList<>();
        CameraMetadataItem item1 = new CameraMetadataItem();
        item1.setId("id1");
        item1.setDescription("camera 1");
        item1.setName("linh");
        item1.setUrl("http://2co2.vp9.tv/chn/DNG33");
        dataItemsToSend.add(item1);
        CameraMetadataItem item2 = new CameraMetadataItem();
        item2.setId("id2");
        item2.setDescription("camera 2");
        item2.setName("vu");
        item2.setUrl("http://2co2.vp9.tv/chn/DNG2");
        dataItemsToSend.add(item2);

        Map<String, String> meta = new HashMap<>();
        Long st_device_change = new Date().getTime();
        meta.put("st_device_change", st_device_change + "");
        CameraInfoTaskPushHINCLocal task;
        task = new CameraInfoTaskPushHINCLocal(args[1], args[2], dataItemsToSend, meta);
        while (true) {
            task.push();
            Thread.sleep(10000);
            task.test();
        }
    }
}
