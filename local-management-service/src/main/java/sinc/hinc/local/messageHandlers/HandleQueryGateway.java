/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.messageHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.factory.MessageClientFactory;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.repository.DAO.orientDB.SoftwareDefinedGatewayDAO;
import sinc.hinc.communication.processing.HINCMessageHander;

/**
 *
 * @author hungld
 */
public class HandleQueryGateway implements HINCMessageHander {

    @Override
    public HincMessage handleMessage(HincMessage msg) {
        System.out.println("Server get request for SDG information: " + msg.toJson());
        Long timeStamp2 = (new Date()).getTime();
        Long timeStamp3 = (new Date()).getTime();
        SoftwareDefinedGatewayDAO gwDAO = new SoftwareDefinedGatewayDAO();
        List<SoftwareDefinedGateway> gws = gwDAO.readAll();
        ObjectMapper mapper = new ObjectMapper();

        try {
            String replyPayload = mapper.writeValueAsString(gws);

            System.out.println("Size of the reply message: " + (replyPayload.length() / 1024) + "KB");
            HincMessage replyMsg = new HincMessage(HINCMessageType.UPDATE_INFORMATION.toString(), HincConfiguration.getMyUUID(), msg.getFeedbackTopic(), "", replyPayload);

            System.out.println("Now send the message back global via topic: " + msg.getFeedbackTopic());

            Long timeStamp4 = (new Date()).getTime(); // time3 -> time4: local service process the data

            replyMsg.hasExtra("timeStamp2", timeStamp2.toString());
            replyMsg.hasExtra("timeStamp3", timeStamp3.toString());
            replyMsg.hasExtra("timeStamp4", timeStamp4.toString());

//            MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
//            FACTORY.getMessagePublisher().pushMessage(replyMsg);
            return replyMsg;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(HandleQueryGateway.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

}
