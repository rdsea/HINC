package sinc.hinc.local.communication.messagehandlers;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.processing.HincMessage;

public class HandleSynRequest implements IMessageHandler{
    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.SYN_REQUEST;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
        //TODO implement MessageHandler
    }
}
