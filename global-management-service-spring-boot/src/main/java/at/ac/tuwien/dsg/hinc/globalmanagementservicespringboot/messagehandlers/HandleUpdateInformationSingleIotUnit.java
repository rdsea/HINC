package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.messagehandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;

public class HandleUpdateInformationSingleIotUnit extends HINCMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public HandleUpdateInformationSingleIotUnit(){
        super(HINCMessageType.UPDATE_INFORMATION_SINGLEIOTUNIT);
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
