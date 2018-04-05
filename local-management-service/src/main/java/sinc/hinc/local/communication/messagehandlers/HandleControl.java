/*
package sinc.hinc.local.communication.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.HincMessage;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.local.communication.LocalCommunicationManager;

public class HandleControl implements IMessageHandler{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LocalCommunicationManager localCommunicationManager;

    public HandleControl(LocalCommunicationManager localCommunicationManager) {
        this.localCommunicationManager = localCommunicationManager;
    }

    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.CONTROL;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
        logger.debug("received " + hincMessage.toString());
        //TODO implement MessageHandler

        localCommunicationManager.sendMessage(null);
    }
}
*/
