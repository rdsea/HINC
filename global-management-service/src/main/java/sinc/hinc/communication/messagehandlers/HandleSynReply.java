package sinc.hinc.communication.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.GlobalCommunicationManager;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.processing.HincMessage;

import java.io.IOException;

public class HandleSynReply implements IMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private GlobalCommunicationManager globalCommunicationManager;

    public HandleSynReply(GlobalCommunicationManager globalCommunicationManager){
        this.globalCommunicationManager = globalCommunicationManager;
    }

    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.SYN_REPLY;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
        logger.debug("received " + hincMessage.toString());

        //TODO implement MessageHandler

        if(hincMessage.getSenderID()!=null && hincMessage.getGroup() != null){
            try {
                String queue = hincMessage.getGroup() + "." + hincMessage.getSenderID();
                globalCommunicationManager.addLocalManagementService(queue, hincMessage.getGroup(), hincMessage.getSenderID());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
