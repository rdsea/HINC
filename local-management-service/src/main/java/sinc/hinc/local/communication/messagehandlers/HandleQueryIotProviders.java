package sinc.hinc.local.communication.messagehandlers;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.processing.HincMessage;

public class HandleQueryIotProviders implements IMessageHandler {

    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.QUERY_IOT_PROVIDERS;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
        //TODO implement MessageHandler
    }
}
