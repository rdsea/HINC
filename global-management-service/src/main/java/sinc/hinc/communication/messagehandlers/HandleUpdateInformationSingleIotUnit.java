package sinc.hinc.communication.messagehandlers;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.processing.HincMessage;

public class HandleUpdateInformationSingleIotUnit implements IMessageHandler {
    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.UPDATE_INFORMATION_SINGLEIOTUNIT;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
        //TODO implement MessageHandler
    }
}
