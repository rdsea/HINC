package sinc.hinc.local.communication.messagehandlers;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.processing.HincMessage;

public class HandleQueryIotUnit implements IMessageHandler{
    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.QUERY_IOT_UNIT;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
        //TODO implement MessageHandler
    }
}
