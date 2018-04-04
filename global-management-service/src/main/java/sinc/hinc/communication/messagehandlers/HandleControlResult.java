package sinc.hinc.communication.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.HINCMessageHandler;
import sinc.hinc.communication.HincMessage;

public class HandleControlResult extends HINCMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    protected HINCMessageType acceptedMessageType() {
        return HINCMessageType.CONTROL_RESULT;
    }

    @Override
    protected void doHandle(HincMessage msg) {
        logger.debug("received " + msg.toString());
        //TODO implement MessageHandler

        //TODO make async
    }
}
