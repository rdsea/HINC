/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.messageHandlers;

import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.factory.MessageClientFactory;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.local.ApplicationMessageType;
import sinc.hinc.local.Main;

/**
 *
 * @author hungld
 */
public class UnsubscribeDataHandler implements HINCMessageHander {

    @Override
    public HincMessage handleMessage(HincMessage message) {
        if (message.getPayload() == null || message.getPayload().isEmpty()) {
            HincMessage replyMsg = new HincMessage(ApplicationMessageType.ACK.toString(), HincConfiguration.getMyUUID(), message.getFeedbackTopic(), "", "You must provide DatapointID and duration for the subscription");
            return replyMsg;
        }
        String sensorID = message.getPayload();
        
        // TODO: implement callback function to stop FORWARDING data
        // Main.listenerLocal.stopForwardingDatapoint(sensorID);

        String replyStr = "Sensor " + sensorID + " is stopped forwarding.";
        HincMessage replyMsg = new HincMessage(ApplicationMessageType.ACK.toString(), HincConfiguration.getMyUUID(), message.getFeedbackTopic(), "", replyStr);
        MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
        FACTORY.getMessagePublisher().pushMessage(replyMsg);
        return replyMsg;
    }

}
