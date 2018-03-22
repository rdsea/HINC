package sinc.hinc.communication;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.processing.HincMessage;

public interface IMessageHandler {
    HINCMessageType getMessageType();
    void handleMessage(HincMessage hincMessage);
}
