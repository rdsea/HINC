package sinc.hinc.local.communication.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.local.communication.LocalCommunicationManager;

public class HandleQueryIotUnit extends HINCMessageHandler{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LocalCommunicationManager localCommunicationManager;

    public HandleQueryIotUnit(LocalCommunicationManager localCommunicationManager){
        super(HINCMessageType.QUERY_IOT_UNIT);
        this.localCommunicationManager = localCommunicationManager;
    }

    @Override
    protected void doHandle(HincMessage msg) {
        logger.debug("received " + msg.toString());
        //TODO implement Handler
    }
}
