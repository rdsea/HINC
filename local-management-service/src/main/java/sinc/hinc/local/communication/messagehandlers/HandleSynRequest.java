/*
package sinc.hinc.local.communication.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.processing.HINCMessageListener;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.local.Main;
import sinc.hinc.local.communication.LocalCommunicationManager;

import java.io.IOException;

public class HandleSynRequest implements IMessageHandler{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LocalCommunicationManager localCommunicationManager;

    public HandleSynRequest(LocalCommunicationManager localCommunicationManager) {
        this.localCommunicationManager = localCommunicationManager;
    }

    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.SYN_REQUEST;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
        logger.debug("received " + hincMessage.toString());
        //TODO implement MessageHandler


        //TODO check if adapted code works / refactor messy code
        HincLocalMeta meta = HincConfiguration.getLocalMeta();
        */
/*System.out.println("GETTING HANDLLER. NUMBER of HANDERS: " + Main.getListener().getHandlers().size());
        for (HINCMessageListener.Handler handler: Main.getListener().getHandlers()){
            System.out.println("adding handler ...: " + handler.getTopic() + " -- " + handler.getMessageType());
            meta.hasHandler(handler.getTopic(), handler.getMessageType(), handler.getHandlerMethod().getClass().getName());
        }*//*

        //TODO add metadata to message: handler.getTopic(), handler.getMessageType(), handler.getClass
        
        HincMessage reply = new HincMessage(HINCMessageType.SYN_REPLY.toString(), HincConfiguration.getMyUUID(), hincMessage.getFeedbackTopic(), "", meta.toJson());
        localCommunicationManager.sendMessage(reply);
    }
}
*/
