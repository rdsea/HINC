package sinc.hinc.local.communication.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.HINCMessageHandler;
import sinc.hinc.communication.HincMessage;
import sinc.hinc.local.communication.LocalCommunicationManager;

public class HandleControl extends HINCMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LocalCommunicationManager localCommunicationManager;

    public HandleControl(LocalCommunicationManager localCommunicationManager) {
        this.localCommunicationManager = localCommunicationManager;
    }

    @Override
    protected void doHandle(HincMessage msg) {
        logger.debug("received " + msg.toString());
        //TODO implement MessageHandler

        //localCommunicationManager.sendToGlobal(null);
    }

    @Override
    protected HINCMessageType acceptedMessageType() {
        return HINCMessageType.CONTROL;
    }
}
