/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.collector;

import at.ac.tuwien.dsg.hinc.collector.utils.HincConfiguration;
import at.ac.tuwien.dsg.hinc.communication.messageInterface.MessageClientFactory;
import at.ac.tuwien.dsg.hinc.communication.messageInterface.MessagePublishInterface;
import at.ac.tuwien.dsg.hinc.communication.messageInterface.MessageSubscribeInterface;
import at.ac.tuwien.dsg.hinc.communication.messageInterface.SalsaMessageHandling;
import at.ac.tuwien.dsg.hinc.communication.messagePayloads.UpdateGatewayStatus;
import at.ac.tuwien.dsg.hinc.communication.protocol.HincMessage;
import at.ac.tuwien.dsg.hinc.communication.protocol.HincMessageTopic;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import at.ac.tuwien.dsg.hinc.model.VirtualNetworkResource.VNF;
import at.ac.tuwien.dsg.hinc.repository.DAO.orientDB.OrientDBConnector;
import at.ac.tuwien.dsg.hinc.repository.DAO.orientDB.SoftwareDefinedGatewayDAO;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import java.io.IOException;
import java.util.Date;
import org.slf4j.Logger;

/**
 * 
 * @author hungld
 */
public class Main {

    static Logger logger = HincConfiguration.getLogger();
    static final MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());

    public static void main(String[] args) throws Exception {
        System.out.println("Starting collector...");
        Long time1 = (new Date()).getTime();        
        OrientDBConnector manager = new OrientDBConnector();
        ODatabaseDocumentTx db = manager.getConnection();    
        Long time2 = (new Date()).getTime();        
        System.out.println("Time to bring up DB is: " + (time2-time1)/1000);

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

        subscribeClientBroadCast.subscribe(HincMessageTopic.CLIENT_REQUEST_DELISE);

        MessageSubscribeInterface subscribeClientUniCast = FACTORY.getMessageSubscriber(new SalsaMessageHandling() {
            @Override
            public void handleMessage(HincMessage msg) {
                if (msg.getMsgType().equals(HincMessage.MESSAGE_TYPE.RPC_QUERY_SDGATEWAY_LOCAL)) {
                    logger.debug("Server get request for SDG information");
                    try {
                        Long timeStamp2 = (new Date()).getTime();
                        // response
                        SoftwareDefinedGateway gw = InfoCollector.getGatewayInfo();                        
                        System.out.println("Collect information done. GW: " + gw.getUuid());
//                        gw.hasCapabilities(InfoCollector.getGatewayConnectivity());
                        
                        Long timeStamp3 = (new Date()).getTime(); // time2 to time3: query provider
                        String replyPayload = gw.toJson();
                        System.out.println("Size of the reply message: " + (replyPayload.length()/1024)+"KB");
                        HincMessage replyMsg = new HincMessage(HincMessage.MESSAGE_TYPE.UPDATE_INFORMATION, HincConfiguration.getMyUUID(), msg.getFeedbackTopic(), "", replyPayload);
                        
                        System.out.println("Now saving data to local repository");
                        SoftwareDefinedGatewayDAO gwDAO = new SoftwareDefinedGatewayDAO();
                        gwDAO.save(gw);
                        
                        Long timeStamp4 = (new Date()).getTime(); // time3 -> time4: local service process the data
                        
                        replyMsg.hasExtra("timeStamp2", timeStamp2.toString());
                        replyMsg.hasExtra("timeStamp3", timeStamp3.toString());
                        replyMsg.hasExtra("timeStamp4", timeStamp4.toString());
                        pub.pushMessage(replyMsg);
                        return;
                    } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                        ex.printStackTrace();
                        logger.error(ex.getMessage());
                    }
                }

                if (msg.getMsgType().equals(HincMessage.MESSAGE_TYPE.RPC_QUERY_NFV_LOCAL)) {
                    logger.debug("Server get request for SDG information");
                    try {
                        // response
                        VNF vnf = InfoCollector.getRouterInfo();
                        vnf.getConnectivities().addAll(InfoCollector.getGatewayConnectivity());
                        String replyPayload = vnf.toJson();
                        HincMessage replyMsg = new HincMessage(HincMessage.MESSAGE_TYPE.UPDATE_INFORMATION, HincConfiguration.getMyUUID(), msg.getFeedbackTopic(), "", replyPayload);
                        pub.pushMessage(replyMsg);
                        return;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        logger.error(ex.getMessage());
                    }
                }

                if (msg.getMsgType().equals(HincMessage.MESSAGE_TYPE.SUBSCRIBE_SDGATEWAY_LOCAL)) {
                    logger.debug("Client wants to subscribe to this collector");
                    InfoMonitor mon = new InfoMonitor();
                    // this will loop forever, thus need to be implement more, e.g. the timeout
                    while (true) {
                        UpdateGatewayStatus update = mon.getSimulatedUpdate();
                        HincMessage replyMsg = new HincMessage(HincMessage.MESSAGE_TYPE.UPDATE_INFORMATION, HincConfiguration.getMyUUID(), msg.getFeedbackTopic(), "", update.toJson());
                        pub.pushMessage(replyMsg);
                    }
                }

                if (msg.getMsgType().equals(HincMessage.MESSAGE_TYPE.SUBSCRIBE_SDGATEWAY_LOCAL_SET_PARAM)) {
                    logger.debug("Set parameter for the subscription");
                    // assume that the payload is: rate;simulatedRatio, e.g. 5;0.2
                    String[] settings = msg.getPayload().split(";");
                    InfoMonitor.monitorRate = Integer.parseInt(settings[0]);
                    InfoMonitor.simulatedChangeRatio = Double.parseDouble(settings[1]);

                }
            }

        });
        subscribeClientUniCast.subscribe(HincMessageTopic.getCollectorTopicByID(HincConfiguration.getMyUUID()));
        subscribeClientUniCast.subscribe(HincMessageTopic.getCollectorTopicBroadcast(HincConfiguration.getGroupName()));

        Long time3 = (new Date()).getTime();        
        logger.info("Local management service startup in "+((double)time3-(double)time2)/1000+" seconds, uuid: " + HincConfiguration.getMyUUID());        

    }
}
