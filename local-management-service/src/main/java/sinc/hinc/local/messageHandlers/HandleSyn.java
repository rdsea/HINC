/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.messageHandlers;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.factory.MessageClientFactory;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.communication.processing.HINCMessageHander;

/**
 * This handler send back a message contains HINC metadata
 *
 * @author hungld
 */
public class HandleSyn implements HINCMessageHander {

    @Override
    public void handleMessage(HincMessage message) {
        String payload = HincConfiguration.getLocalMeta().toJson();
        HincMessage replyMsg = new HincMessage(HINCMessageType.SYN_REPLY.toString(), HincConfiguration.getMyUUID(), message.getFeedbackTopic(), "", payload);
        MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
        FACTORY.getMessagePublisher().pushMessage(replyMsg);
    }

}
