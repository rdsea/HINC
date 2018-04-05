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

public class HandleUpdateInfoBase implements IMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LocalCommunicationManager localCommunicationManager;

    public HandleUpdateInfoBase(LocalCommunicationManager localCommunicationManager) {
        this.localCommunicationManager = localCommunicationManager;
    }

    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.UPDATE_INFO_BASE;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
        logger.debug("received " + hincMessage.toString());
        //TODO implement MessageHandler


        //TODO check if adapted code works / refactor messy code

        System.out.println("Handling update info base...");
        // if there is a clear command in extra, flush the list of namespace
        if (hincMessage.getExtra() == null || !hincMessage.getExtra().containsKey("append")){
            HincConfiguration.getLocalMeta().clearInfoBase();
        }
        // add namespace
        String infobase = hincMessage.getPayload();
        System.out.println("Infobase send to me: " + infobase);
        String[] infobase_array = infobase.split(",");
        for(String s: infobase_array){
            HincConfiguration.getLocalMeta().hasInfoBase(s.trim());
        }

        //TODO check this questionable piece of code
        // update handlers to send back the metadata of HINC local
        HincLocalMeta meta = HincConfiguration.getLocalMeta();
        */
/*System.out.println("GETTING HANDLLER. NUMBER of HANDERS: " + Main.getListener().getHandlers().size());
        for (HINCMessageListener.Handler handler: Main.getListener().getHandlers()){
            System.out.println("adding handler ...: " + handler.getTopic() + " -- " + handler.getMessageType());
            meta.hasHandler(handler.getTopic(), handler.getMessageType(), handler.getHandlerMethod().getClass().getName());
        }*//*


        //TODO add metadata to message: handler.getTopic(), handler.getMessageType(), handler.getClass

        HincMessage reply =  new HincMessage(HINCMessageType.SYN_REPLY.toString(), HincConfiguration.getMyUUID(), hincMessage.getFeedbackTopic(), "", meta.toJson());
        localCommunicationManager.sendMessage(reply);
    }
}
*/
