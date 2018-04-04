package sinc.hinc.local.communication.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.HincMessage;
import sinc.hinc.local.communication.LocalCommunicationManager;

public class HandleQueryIotUnit implements IMessageHandler{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LocalCommunicationManager localCommunicationManager;

    public HandleQueryIotUnit(LocalCommunicationManager localCommunicationManager){
        this.localCommunicationManager = localCommunicationManager;
    }

    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.QUERY_IOT_UNIT;
    }

    @Override
    public void handleMessage(HincMessage hincMessage) {
    }
}
