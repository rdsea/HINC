package sinc.hinc.local.communication.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.processing.HincMessage;

public class HandleUpdateInfoBase implements IMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.UPDATE_INFO_BASE;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
        logger.debug("received " + hincMessage.toString());
        //TODO implement MessageHandler
    }
}
