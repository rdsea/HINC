package sinc.hinc.local.messageHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

import java.util.Arrays;
import java.util.List;

public class HandleQueryCloudService implements HINCMessageHander {
    static Logger logger = HincConfiguration.getLogger();
    
    @Override
    public HincMessage handleMessage(HincMessage hincMessage) {
        logger.debug("Server get request for CloudService information: "+ hincMessage.toJson());

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
                return null;
            }
        }

        int limit = -1;
        if (hincMessage.getExtra() != null && hincMessage.getExtra().containsKey("limit")) {
            try {
                limit = Integer.parseInt(hincMessage.getExtra().get("limit"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        AbstractDAO<CloudService> cloudServiceDAO = new AbstractDAO<CloudService>(CloudService.class);

        List<CloudService> cloudServices = cloudServiceDAO.readAll(limit);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String replyPayload = mapper.writeValueAsString(cloudServices);
            logger.debug("Size of the reply message: " + (replyPayload.length() / 1024) + "KB");
            HincMessage reply = new HincMessage(
                    HINCMessageType.UPDATE_INFORMATION_CLOUDSERVICE.toString(),
                    HincConfiguration.getMyUUID(),
                    hincMessage.getFeedbackTopic(),
                    "",
                    replyPayload
            );
            logger.debug("Now send the message back global via topic: " + hincMessage.getFeedbackTopic());
            return reply;


        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }
}
