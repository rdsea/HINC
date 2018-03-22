package sinc.hinc.local.communication.messagehandlers;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.processing.HincMessage;

public class HandleUpdateInfoBase implements IMessageHandler {
    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.UPDATE_INFO_BASE;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
        //TODO implement MessageHandler
    }
}
