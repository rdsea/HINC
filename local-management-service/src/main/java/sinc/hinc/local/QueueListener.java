/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import sinc.hinc.common.metadata.HincMessage;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.messageInterface.MessagePublishInterface;
import sinc.hinc.communication.messageInterface.MessageSubscribeInterface;
import sinc.hinc.communication.messageInterface.SalsaMessageHandling;
import static sinc.hinc.local.Main.FACTORY;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;
import sinc.hinc.repository.DAO.orientDB.SoftwareDefinedGatewayDAO;

/**
 *
 * @author hungld
 */
public class QueueListener {

    static Logger logger = HincConfiguration.getLogger();

    public static void listenSynMessage() {
        final MessagePublishInterface pub = FACTORY.getMessagePublisher();

        MessageSubscribeInterface subscribeClientBroadCast = FACTORY.getMessageSubscriber(new SalsaMessageHandling() {
            @Override
            public void handleMessage(HincMessage msg) {
                if (msg.getMsgType().equals(HincMessage.MESSAGE_TYPE.SYN_REQUEST)) {
                    String payload = HincConfiguration.getMeta().toJson();
                    HincMessage replyMsg = new HincMessage(HincMessage.MESSAGE_TYPE.SYN_REPLY, HincConfiguration.getMyUUID(), HincMessageTopic.REGISTER_AND_HEARBEAT, "", payload);
                    pub.pushMessage(replyMsg);

                }
            }
        }); // end new SalsaMessageHandling

        subscribeClientBroadCast.subscribe(HincMessageTopic.CLIENT_REQUEST_HINC);
    }

    public static void listenQueryMessage() {
        final MessagePublishInterface pub = FACTORY.getMessagePublisher();
        MessageSubscribeInterface subscribeClientUniCast = FACTORY.getMessageSubscriber(new SalsaMessageHandling() {
            @Override
            public void handleMessage(HincMessage msg) {
                if (msg.getMsgType().equals(HincMessage.MESSAGE_TYPE.RPC_QUERY_SDGATEWAY_LOCAL)) {
                    logger.debug("Server get request for SDG information: " + msg.toJson());
                    Long timeStamp2 = (new Date()).getTime();
                    Long timeStamp3 = (new Date()).getTime();
                    SoftwareDefinedGatewayDAO gwDAO = new SoftwareDefinedGatewayDAO();
                    List<SoftwareDefinedGateway> gws = gwDAO.readAll();
                    for (SoftwareDefinedGateway gw : gws) {
                        String replyPayload = gw.toJson();
                        System.out.println("Size of the reply message: " + (replyPayload.length() / 1024) + "KB");
                        HincMessage replyMsg = new HincMessage(HincMessage.MESSAGE_TYPE.UPDATE_INFORMATION, HincConfiguration.getMyUUID(), msg.getFeedbackTopic(), "", replyPayload);

                        System.out.println("Now send the message back global via topic: " + msg.getFeedbackTopic());

                        Long timeStamp4 = (new Date()).getTime(); // time3 -> time4: local service process the data

                        replyMsg.hasExtra("timeStamp2", timeStamp2.toString());
                        replyMsg.hasExtra("timeStamp3", timeStamp3.toString());
                        replyMsg.hasExtra("timeStamp4", timeStamp4.toString());
                        pub.pushMessage(replyMsg);
                    }
                    return;
                }

                if (msg.getMsgType().equals(HincMessage.MESSAGE_TYPE.RPC_QUERY_NFV_LOCAL)) {
                    logger.debug("Server get request for SDG information");
                    try {
                        AbstractDAO<NetworkFunctionService> vnfDAO = new AbstractDAO<>(NetworkFunctionService.class);
                        List<NetworkFunctionService> listOfNFS = vnfDAO.readAll();
                        for (NetworkFunctionService nfs : listOfNFS) {
                            String replyPayload = nfs.toJson();
                            HincMessage replyMsg = new HincMessage(HincMessage.MESSAGE_TYPE.UPDATE_INFORMATION, HincConfiguration.getMyUUID(), msg.getFeedbackTopic(), "", replyPayload);
                            pub.pushMessage(replyMsg);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        logger.error(ex.getMessage());
                    }
                    return;
                }
            }

        });
        subscribeClientUniCast.subscribe(HincMessageTopic.getCollectorTopicByID(HincConfiguration.getMyUUID()));
        subscribeClientUniCast.subscribe(HincMessageTopic.getCollectorTopicBroadcast(HincConfiguration.getGroupName()));
    }

}
