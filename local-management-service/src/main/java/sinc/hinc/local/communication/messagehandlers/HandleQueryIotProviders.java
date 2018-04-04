package sinc.hinc.local.communication.messagehandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.HincMessage;
import sinc.hinc.communication.IMessageHandler;
import sinc.hinc.local.LocalManagementService;
import sinc.hinc.local.communication.LocalCommunicationManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HandleQueryIotProviders implements IMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LocalCommunicationManager localCommunicationManager;

    public HandleQueryIotProviders(LocalCommunicationManager localCommunicationManager) {
        this.localCommunicationManager = localCommunicationManager;
    }

    @Override
    public HINCMessageType getMessageType() {
        return HINCMessageType.QUERY_IOT_PROVIDERS;
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
                if (myInfoBases.contains(s.trim())) {
                    found = true;
                    break;
                }
            }
            if (found == false) {
                // this local service does not in the Information Bases of the query, so just pass.
                return;
            }
        }

        if (hincMessage.getPayload().contains("rescan")) {
            LocalManagementService.scanAdaptors();
        }

        int limit = -1;
        if (hincMessage.getExtra() != null && hincMessage.getExtra().containsKey("limit")) {
            try {
                limit = Integer.parseInt(hincMessage.getExtra().get("limit"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }


    }
}
