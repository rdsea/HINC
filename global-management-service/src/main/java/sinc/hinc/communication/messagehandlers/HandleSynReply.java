package sinc.hinc.communication.messagehandlers;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.processing.HincMessage;

public class HandleSynReply implements IMessageHandler {
    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.SYN_REPLY;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
        //TODO implement MessageHandler
    }
}
