/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.metadata.HincMessage;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.communication.messageInterface.MessageClientFactory;
import sinc.hinc.communication.messageInterface.MessagePublishInterface;
import sinc.hinc.communication.messageInterface.MessageSubscribeInterface;
import sinc.hinc.communication.messageInterface.SalsaMessageHandling;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import sinc.hinc.model.VirtualNetworkResource.VNF;

import java.util.ArrayList;
import java.util.List;

/**
 * The class send messages from HINC Global to multiple HINC Local.
 *
 * @author hungld
 */
public class CommunicationManager {

    static Logger logger = LoggerFactory.getLogger("HINC");
    MessageClientFactory FACTORY;
    String prefixTopic = "";
    List<HincLocalMeta> listOfHINCLocal = new ArrayList<>();
    String groupName;

    /**
     * To create a communication channel
     *
     * @param groupName  The group to communication within
     * @param broker     The endpoint of the broker
     * @param brokerType The type of the broker like mqtt, ampq
     */
    public CommunicationManager(String groupName, String broker, String brokerType) {
        this.groupName = groupName;
        this.FACTORY = new MessageClientFactory(broker, brokerType);
    }

    public void synFunctionCallBroadcast(int timeout, String feedBackTopicToSubscribe, HincMessage.MESSAGE_TYPE messageType, String publishTopic, SalsaMessageHandling handler) {
        MessageSubscribeInterface sub = FACTORY.getMessageSubscriber(handler);
        logger.debug("Will subscribe to the topic: " + feedBackTopicToSubscribe + " to wait for the reply");
        sub.subscribe(feedBackTopicToSubscribe, timeout);
        MessagePublishInterface pub = FACTORY.getMessagePublisher();
        HincMessage requestMessage = new HincMessage(messageType, this.groupName, publishTopic, feedBackTopicToSubscribe, "");
        pub.pushMessage(requestMessage);
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("Done, should close the subscribe now.. ");
    }

    // with a single UUID we don't need timeout. The result can only String, where
    public String synFunctionCallUnicast(String hincUUID, HincMessage.MESSAGE_TYPE messageType) {
        logger.debug("Trying to query HINC Local with ID: " + hincUUID);
        String unicastHINCTopic = HincMessageTopic.getCollectorTopicByID(hincUUID);

        MessagePublishInterface pub = FACTORY.getMessagePublisher();
        // note that when using callFunction, no need to declare the feedbackTopic. This will be filled by the call
        HincMessage queryMessage = new HincMessage(messageType, groupName, unicastHINCTopic, "", "");
        logger.debug("Calling the function: " + queryMessage.toJson());
        HincMessage responseMessage = pub.callFunction(queryMessage);
        logger.debug("Function called and return the result: " + responseMessage.getPayload());
        return responseMessage.getPayload();
    }


    @Deprecated
    public VNF queryVNF_Unicast(String hincUUID) {
        String unicastDeliseTopic = HincMessageTopic.getCollectorTopicByID(hincUUID);

        MessagePublishInterface pub = FACTORY.getMessagePublisher();
        // note that when using callFunction, no need to declare the feedbackTopic. This will be filled by the call
        HincMessage queryMessage = new HincMessage(HincMessage.MESSAGE_TYPE.RPC_QUERY_NFV_LOCAL, groupName, unicastDeliseTopic, "", "");
        HincMessage responseMessage = pub.callFunction(queryMessage);
        String vnfInfo = responseMessage.getPayload();
        if (vnfInfo.equals("null")) {
            logger.debug("Delise " + hincUUID + " does not return a gateway info");
            return null;
        }
        logger.debug("Get VNF info: \n" + vnfInfo);
        System.out.println("Get VNF info: \n" + vnfInfo);
        return VNF.fromJson(vnfInfo);
    }


    public String getName() {
        return groupName;
    }

    public List<HincLocalMeta> getListOfHINCLocal() {
        return listOfHINCLocal;
    }

    public void SendControl(ControlPoint controlPoint) {
        // TODO: implement this
    }

}
