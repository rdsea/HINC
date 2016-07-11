/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.messageHandlers;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HINCMessageListener;
import sinc.hinc.local.Main;

/**
 * This handler send back a message contains HINC metadata
 *
 * @author hungld
 */
public class HandleSyn implements HINCMessageHander {

    @Override
    public HincMessage handleMessage(HincMessage message) {
        HincLocalMeta meta = HincConfiguration.getLocalMeta();
        System.out.println("GETTING HANDLLER. NUMBER of HANDERS: " + Main.getListener().getHandlers().size());
        for (HINCMessageListener.Handler handler: Main.getListener().getHandlers()){
            System.out.println("adding handler ...: " + handler.getTopic() + " -- " + handler.getMessageType());
            meta.hasHandler(handler.getTopic(), handler.getMessageType(), handler.getHandlerMethod().getClass().getName());
        }
        return new HincMessage(HINCMessageType.SYN_REPLY.toString(), HincConfiguration.getMyUUID(), message.getFeedbackTopic(), "", meta.toJson());
//        MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
//        FACTORY.getMessagePublisher().pushMessage(replyMsg);
//        return replyMsg;
    }

}
