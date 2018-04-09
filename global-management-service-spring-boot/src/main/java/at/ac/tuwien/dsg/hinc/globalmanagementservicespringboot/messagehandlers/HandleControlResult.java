package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;

public class HandleControlResult extends HINCMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public HandleControlResult() {
        super(HINCMessageType.CONTROL_RESULT);
    }

    @Override
    protected void doHandle(HincMessage msg) {
        logger.debug("received " + msg.toString());
        //TODO implement MessageHandler

        //TODO make async
    }
}
