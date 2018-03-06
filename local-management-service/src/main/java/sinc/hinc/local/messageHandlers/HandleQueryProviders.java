/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.messageHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.local.LocalManagementService;
import sinc.hinc.model.API.WrapperProvider;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

/**
 *
 * @author hungld
 */
public class HandleQueryProviders implements HINCMessageHander {

    static Logger logger = HincConfiguration.getLogger();

    @Override
    public HincMessage handleMessage(HincMessage msg) {
        logger.debug("Querying resource provider !");

        // check infobase
        if (msg.getExtra() != null && msg.getExtra().containsKey("infoBases")) {
            List<String> infoBasesList = Arrays.asList(msg.getExtra().get("infoBases").split(","));
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
                return null;
            }
        }

        if (msg.getPayload().contains("rescan")) {
            LocalManagementService.scanAdaptors();
        }
        
        int limit = -1;
        if (msg.getExtra() != null && msg.getExtra().containsKey("limit")) {
            try {
                limit = Integer.parseInt(msg.getExtra().get("limit"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        AbstractDAO<ResourcesProvider> rpDAO = new AbstractDAO<>(ResourcesProvider.class);
        List<ResourcesProvider> providerList = rpDAO.readAll(limit);
        WrapperProvider wrapper = new WrapperProvider(providerList);
        ObjectMapper mapper = new ObjectMapper();
        String replyPayload;
        try {
            replyPayload = mapper.writeValueAsString(wrapper);
            logger.debug("Size of the reply message: " + (replyPayload.length() / 1024) + "KB");
            HincMessage replyMsg = new HincMessage(HINCMessageType.UPDATE_INFORMATION_SINGLEIOTUNIT.toString(), HincConfiguration.getMyUUID(), msg.getFeedbackTopic(), "", replyPayload);
            logger.debug("Resource provider reply: " + replyMsg.toJson());
            return replyMsg;
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }

    }

}
