/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.provider.tasks;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HINCMessageSender;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.dummyprovider.plugin.DummyProviderTransformer;
import sinc.hinc.dummyprovider.provider.DummyData;
import sinc.hinc.dummyprovider.provider.DummyMetadataItem;
import sinc.hinc.dummyprovider.provider.DummyREST;
import sinc.hinc.model.API.WrapperIoTUnit;

/**
 *
 * @author hungld
 */
public class DummyTaskPushHINCLocal {

    static Logger logger = LoggerFactory.getLogger("Dummy");
    DummyData allData;
    List<DummyMetadataItem> dataItems;
    Map<String, String> meta = null;

    public DummyTaskPushHINCLocal(DummyData allDummyData, List<DummyMetadataItem> dataItemsToSend, Map<String, String> meta) {
        this.allData = allDummyData;
        this.dataItems = dataItemsToSend;
        this.meta = meta;
    }

    public void push() {
        DummyProviderTransformer transformer = new DummyProviderTransformer();
        WrapperIoTUnit wrapper = new WrapperIoTUnit();
        for (DummyMetadataItem data : dataItems) {
            wrapper.getUnits().add(transformer.translateIoTUnit(data));
        }
        if (DummyREST.changePolicy.getAmqp() != null && DummyREST.changePolicy.getAmqp() != null) {
            HINCMessageSender comMng = new HINCMessageSender(DummyREST.changePolicy.getAmqp(), "amqp");
            HincMessage msg = new HincMessage(HINCMessageType.PROVIDER_UPDATE_IOT_UNIT.toString(), "DummyProvider", HincMessageTopic.getBroadCastTopic(DummyREST.changePolicy.getAmqpgroup()), "", wrapper.toJson());
            msg.hasExtra("test", "experiment");
            msg.getExtra().putAll(meta);
            comMng.asynCall(0, msg, new HINCMessageHander() {
                @Override
                public HincMessage handleMessage(HincMessage message) {
                    logger.debug("Provider published message ! But will not recieve any reply. This message will never be shown!");
                    return null;
                }
            });
        }
    }
}
