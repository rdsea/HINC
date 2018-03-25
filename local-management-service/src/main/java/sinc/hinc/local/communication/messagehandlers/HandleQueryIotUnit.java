package sinc.hinc.local.communication.messagehandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.local.LocalManagementService;
import sinc.hinc.local.communication.LocalCommunicationManager;
import sinc.hinc.model.API.WrapperIoTUnit;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.repository.DAO.orientDB.IoTUnitDAO;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
        logger.debug("received " + hincMessage.toString());
        //TODO implement MessageHandler

        //TODO check if adapted code works / refactor messy code
        // check infobase
        if (hincMessage.getExtra() != null && hincMessage.getExtra().containsKey("infoBases")) {
            List<String> infoBasesList = Arrays.asList(hincMessage.getExtra().get("infoBases").split(","));
            List<String> myInfoBases = HincConfiguration.getLocalMeta().getInfoBase();
            boolean found = false;
            for (String s : infoBasesList) {
                if (myInfoBases.contains(s.trim())){
                    found = true;
                    break;
                }
            }
            if (found == false) {
                // this local service does not in the Information Bases of the query, so just pass.
                return;
            }
        }

        // processing
        Long timeStamp2 = (new Date()).getTime();

        if (hincMessage.getPayload().contains("rescan")) {
            LocalManagementService.scanAdaptors();
        }

        Long timeStamp3 = (new Date()).getTime();
        IoTUnitDAO unitDao = new IoTUnitDAO();
        int limit = -1;
        if (hincMessage.getExtra() != null && hincMessage.getExtra().containsKey("limit")) {
            try {
                limit = Integer.parseInt(hincMessage.getExtra().get("limit"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        List<IoTUnit> units = unitDao.readAll(limit);
        logger.debug("Local service read all : " + units.size() + " items, which will be send back.");
        WrapperIoTUnit wrapper = new WrapperIoTUnit(units);
        ObjectMapper mapper = new ObjectMapper();

        try {
            String replyPayload = mapper.writeValueAsString(wrapper);

            logger.debug("Size of the reply message: " + (replyPayload.length() / 1024) + "KB");
            HincMessage replyMsg = new HincMessage(HINCMessageType.UPDATE_INFORMATION_SINGLEIOTUNIT.toString(), HincConfiguration.getMyUUID(), hincMessage.getFeedbackTopic(), "", replyPayload);

            System.out.println("Now send the message back global via topic: " + hincMessage.getFeedbackTopic());

            Long timeStamp4 = (new Date()).getTime(); // time3 -> time4: local service process the data

            replyMsg.hasExtra("timeStamp2", timeStamp2.toString());
            replyMsg.hasExtra("timeStamp3", timeStamp3.toString());
            replyMsg.hasExtra("timeStamp4", timeStamp4.toString());

            localCommunicationManager.sendToGlobal(replyMsg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
