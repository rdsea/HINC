/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.messageHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.factory.MessageClientFactory;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.model.VirtualNetworkResource.NetworkService;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;
import sinc.hinc.communication.processing.HINCMessageHander;

/**
 *
 * @author hungld
 */
public class HandleQueryVNF implements HINCMessageHander {

    @Override
    public HincMessage handleMessage(HincMessage msg) {
        System.out.println("Server get request for SDG information");
        try {
            AbstractDAO<NetworkService> vnfDAO = new AbstractDAO<>(NetworkService.class);
            List<NetworkService> listOfNFS = vnfDAO.readAll();
            ObjectMapper mapper = new ObjectMapper();
            String replyPayload = mapper.writeValueAsString(listOfNFS);
            return new HincMessage(HINCMessageType.UPDATE_INFORMATION_SINGLEIOTUNIT.toString(), HincConfiguration.getMyUUID(), msg.getFeedbackTopic(), "", replyPayload);
//            MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
//            FACTORY.getMessagePublisher().pushMessage(replyMsg);
//            return replyMsg;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
