package sinc.hinc.communication.messagehandlers;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.processing.HincMessage;

public class HandleControlResult implements IMessageHandler {
    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.CONTROL_RESULT;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
        //TODO implement MessageHandler
    }
}
