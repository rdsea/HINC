package sinc.hinc.communication.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.communication.HINCMessageHandler;
import sinc.hinc.communication.HincMessage;

public class HandleUpdateInformationSingleIotUnit extends HINCMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected HINCMessageType acceptedMessageType() {
        return HINCMessageType.UPDATE_INFORMATION_SINGLEIOTUNIT;
    }

    @Override
    protected void doHandle(HincMessage hincMessage) {
        logger.debug("received " + hincMessage.toString());
        //TODO implement MessageHandler
        /*System.out.println("A single IoT Unit is push the HINC global, from: " + hincMessage.getSenderID() + ". Content:" + hincMessage.getPayload());
        IoTUnit unit = IoTUnit.fromJson(hincMessage.getPayload());
        IoTUnitDAO dao = new IoTUnitDAO();
        dao.save(unit);*/
    }
}
