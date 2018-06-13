package at.ac.tuwien.dsg.hinc.globalmanagementservice.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;

@Component
public class HandleControlResult extends HINCMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public HandleControlResult() {
        super(HINCMessageType.CONTROL_RESULT);
    }

    @Override
    protected HincMessage doHandle(HincMessage msg) {
        logger.debug("received " + msg.toString());
        //TODO implement MessageHandler
        return null;
    }
}
