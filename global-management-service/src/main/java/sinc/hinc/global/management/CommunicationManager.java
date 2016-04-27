/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.management;

import sinc.hinc.repository.DAO.orientDB.SoftwareDefinedGatewayDAO;
import sinc.hinc.abstraction.ResourceDriver.InfoSourceSettings;
import sinc.hinc.communication.messageInterface.MessageClientFactory;
import sinc.hinc.communication.messageInterface.MessagePublishInterface;
import sinc.hinc.communication.messageInterface.MessageSubscribeInterface;
import sinc.hinc.communication.messageInterface.SalsaMessageHandling;
import sinc.hinc.communication.messagePayloads.HincMeta;
import sinc.hinc.communication.protocol.HincMessage;
import sinc.hinc.communication.protocol.HincMessageTopic;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.model.VirtualNetworkResource.VNF;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class send messages from HINC Global to multiple HINC Local.
 *
 * @author hungld
 */
public class CommunicationManager {

    MessageClientFactory FACTORY;
    String prefixTopic = "";
    static Logger logger = LoggerFactory.getLogger("HINC");

    List<HincMeta> listOfHINCLocal = new ArrayList<>();
    String groupName;

    /**
     * To create a communication channel
     *
     * @param groupName The group to communication within
     * @param broker The endpoint of the broker
     * @param brokerType The type of the broker like mqtt, ampq
     */
    public CommunicationManager(String groupName, String broker, String brokerType) {
        this.groupName = groupName;
        this.FACTORY = new MessageClientFactory(broker, brokerType);
    }

    /**
     * This broadcast a message, block for timeout seconds, update the list
     *
     * @param timeout The time to wait response from other HINC
     * @return a list of Local Management Services running
     */
    public List<HincMeta> synHINC(long timeout) {
        logger.debug("Start syn delise...");
        listOfHINCLocal.clear();

        MessageSubscribeInterface sub = FACTORY.getMessageSubscriber(new SalsaMessageHandling() {
            @Override
            public void handleMessage(HincMessage msg) {
                logger.debug("A message arrive, from: {}, type: {}, topic: {} ", msg.getFromSalsa(), msg.getMsgType(), msg.getTopic());
                if (msg.getMsgType().equals(HincMessage.MESSAGE_TYPE.SYN_REPLY)) {
                    logger.debug("Yes, it is a SYN message, adding the metadata");
                    HincMeta meta = HincMeta.fromJson(msg.getPayload());
                    logger.debug("Meta: " + meta.toJson());
                    listOfHINCLocal.add(meta);
                }
                logger.debug("Add meta finished");
            }
        });
        logger.debug("Will subscribe to the topic");
        sub.subscribe(HincMessageTopic.REGISTER_AND_HEARBEAT, timeout);
        logger.debug("Subscribe done, now waiting for SYN message");

        MessagePublishInterface pub = FACTORY.getMessagePublisher();
        HincMessage synRequestMsg = new HincMessage(HincMessage.MESSAGE_TYPE.SYN_REQUEST, this.groupName, HincMessageTopic.CLIENT_REQUEST_HINC, "", "");
        logger.debug("Client starts to send SYN message to many DESLISE...");
        pub.pushMessage(synRequestMsg);
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        logger.debug("Done, should close the subscribe now.. ");
        // write to cache

        AbstractDAO<HincMeta> metaDAO = new AbstractDAO<>(HincMeta.class);
        metaDAO.deleteAll();
        metaDAO.saveAll(listOfHINCLocal);

        return listOfHINCLocal;
    }

    /**
     * This method read DB for Local management service, if not found it run the syn command.
     *
     * @return
     */
    public List<HincMeta> getListOfLocalManagementServicesFromDBorQuery() {
        AbstractDAO<HincMeta> metaDAO = new AbstractDAO<>(HincMeta.class);
        List<HincMeta> list = metaDAO.readAll();
        if (list == null || list.isEmpty()) {
            // try to run the syn command
            logger.debug("There is no local-management-service found, try to run a SYN ...");
            list = synHINC(3000);
        }

        if (list == null || list.isEmpty()) {
            logger.debug("No local-management-service found. Cannot send query");
            return null;
        }
        this.listOfHINCLocal.clear();
        this.listOfHINCLocal = list;
        return list;
    }

    public SoftwareDefinedGateway querySoftwareDefinedGateway_Unicast(String hincUUID) {
        logger.debug("Trying to query DELISE with ID: " + hincUUID);
        String unicastDeliseTopic = HincMessageTopic.getCollectorTopicByID(hincUUID);

        MessagePublishInterface pub = FACTORY.getMessagePublisher();
        // note that when using callFunction, no need to declare the feedbackTopic. This will be filled by the call
        HincMessage queryMessage = new HincMessage(HincMessage.MESSAGE_TYPE.RPC_QUERY_SDGATEWAY_LOCAL, groupName, unicastDeliseTopic, "", "");
        logger.debug("Calling the function: " + queryMessage.toJson());
        HincMessage responseMessage = pub.callFunction(queryMessage);
        logger.debug("Query done !");
        String gatewayInfo = responseMessage.getPayload();
        if (gatewayInfo.equals("null")) {
            logger.debug("Delise " + hincUUID + " does not return a gateway info");
            return null;
        }
        logger.debug("Get SDG info: \n" + gatewayInfo);
        System.out.println("Get SDG info: \n" + gatewayInfo);
        return SoftwareDefinedGateway.fromJson(gatewayInfo);
    }

    public VNF queryVNF_Unicast(String hincUUID) {
        String unicastDeliseTopic = HincMessageTopic.getCollectorTopicByID(hincUUID);

        MessagePublishInterface pub = FACTORY.getMessagePublisher();
        // note that when using callFunction, no need to declare the feedbackTopic. This will be filled by the call
        HincMessage queryMessage = new HincMessage(HincMessage.MESSAGE_TYPE.RPC_QUERY_NFV_LOCAL, groupName, unicastDeliseTopic, "", "");
        HincMessage responseMessage = pub.callFunction(queryMessage);
        String vnfInfo = responseMessage.getPayload();
        if (vnfInfo.equals("null")) {
            logger.debug("Delise " + hincUUID + " does not return a gateway info");
            return null;
        }
        logger.debug("Get VNF info: \n" + vnfInfo);
        System.out.println("Get VNF info: \n" + vnfInfo);
        return VNF.fromJson(vnfInfo);
    }

    @Deprecated
    public List<SoftwareDefinedGateway> querySoftwareDefinedGateway_Multicast() {
        List<HincMeta> localManagementServicesList = getListOfLocalManagementServicesFromDBorQuery();
        List<SoftwareDefinedGateway> gateways = new ArrayList<>();
        System.out.println("Number of gateway syn: " + localManagementServicesList.size() + ". Now start unicast querying information...");

        for (HincMeta meta : localManagementServicesList) {
            logger.debug("Checking delise id: " + meta.getUuid() + ", ip: " + meta.getIp());
            InfoSourceSettings settings = InfoSourceSettings.fromJson(meta.getSettings());
            if (settings.getSource() != null && !settings.getSource().isEmpty()) {
                InfoSourceSettings.InfoSource firstSource = settings.getSource().get(0);
                if (firstSource.getType().equals(InfoSourceSettings.ProviderType.IoT)) {
                    logger.debug("It is a gateway, the HINC id: " + meta.getUuid() + ", ip: " + meta.getIp());
                    SoftwareDefinedGateway g = querySoftwareDefinedGateway_Unicast(meta.getUuid());
                    gateways.add(g);
                } else {
                    logger.debug("Delise is: " + meta.getUuid() + " is not a gateway !");
                }
            }
        }

        logger.debug("Now start to write the list of gateway to database ....");
        SoftwareDefinedGatewayDAO gwDAO = new SoftwareDefinedGatewayDAO();
        gwDAO.saveAll(gateways);

//        (new CacheGateway()).writeGatewayCache(gateways);
        return gateways;
    }

    @Deprecated
    public List<VNF> queryVNF_Multicast() {
        List<HincMeta> delises = getListOfLocalManagementServicesFromDBorQuery();
        List<VNF> routers = new ArrayList<>();
        System.out.println("Number of router syn: " + delises.size() + ". Now start unicast querying information...");

        for (HincMeta delise : delises) {
            logger.debug("Checking router id: " + delise.getUuid() + ", ip: " + delise.getIp());
            InfoSourceSettings settings = InfoSourceSettings.fromJson(delise.getSettings());
            if (settings.getSource() != null && !settings.getSource().isEmpty()) {
                InfoSourceSettings.InfoSource firstSource = settings.getSource().get(0);
                if (firstSource.getType().equals(InfoSourceSettings.ProviderType.Network)) {
                    logger.debug("It is a router, the delise id: " + delise.getUuid() + ", ip: " + delise.getIp());
                    VNF g = queryVNF_Unicast(delise.getUuid());
                    routers.add(g);
                } else {
                    logger.debug("Delise is: " + delise.getUuid() + " is not a gateway !");
                }
            }

        }

        logger.debug("Now start to write the list of router to cache ....");
        AbstractDAO<VNF> vnfDAO = new AbstractDAO<>(VNF.class);
        vnfDAO.saveAll(routers);

//        (new CacheVNF()).writeGatewayCache(routers);
        return routers;
    }

    /**
     * This function broadcast a message and wait for a few seconds Also, it record the last message to log file
     *
     * @param timeout
     * @return
     */
    public List<SoftwareDefinedGateway> querySoftwareDefinedGateway_Broadcast(long timeout) {
        System.out.println("Start broadcasting the query...");
        File dir = new File("log/queries");
        dir.mkdirs();
        System.out.println("Data is stored in: " + dir.getAbsolutePath());

        final List<String> events = new LinkedList<>();
        final List<SoftwareDefinedGateway> result = new ArrayList<>();

        MessagePublishInterface pub = FACTORY.getMessagePublisher();
        // note that when using callFunction, no need to declare the feedbackTopic. This will be filled by the call
        String feedBackTopic = HincMessageTopic.getTemporaryTopic();

        final long timeStamp1 = (new Date()).getTime();
        String eventFileName = "log/queries/" + timeStamp1 + ".event";

        MessageSubscribeInterface sub = FACTORY.getMessageSubscriber(new SalsaMessageHandling() {
            long latestTime = 0;
            long quantity = 0;
            long currentSum = 0;

            @Override
            public void handleMessage(HincMessage message) {
                Long timeStamp5 = (new Date()).getTime();
                System.out.println("Get a response message from " + message.getFromSalsa());
                SoftwareDefinedGateway gw = SoftwareDefinedGateway.fromJson(message.getPayload());
                if (gw == null) {
                    System.out.println("Payload is null, or cannot be converted");
                    return;
                }
                SoftwareDefinedGatewayDAO gwDAO = new SoftwareDefinedGatewayDAO();
//                gwDAO.save(gw);
                result.add(gw);

                // ==== Record time for various experiments ===
                Long timeStamp6 = (new Date()).getTime();
                Long timeStamp2 = Long.parseLong(message.getExtra().get("timeStamp2"));
                Long timeStamp3 = Long.parseLong(message.getExtra().get("timeStamp3"));
                Long timeStamp4 = Long.parseLong(message.getExtra().get("timeStamp4"));

                Long local_global_latency = timeStamp2 - timeStamp1;
                Long provider_process = timeStamp3 - timeStamp2;
                Long local_process = timeStamp4 - timeStamp3;
                Long reply_latency = timeStamp5 - timeStamp4;
                Long global_latency = timeStamp6 - timeStamp5;
                Long end2end = timeStamp6 - timeStamp1;

                String eventStr = gw.getUuid() + "," + timeStamp1 + "," + timeStamp2 + "," + timeStamp3 + "," + timeStamp4 + "," + timeStamp5 + "," + timeStamp6 + ","
                        + local_global_latency + "," + provider_process + "," + local_process + "," + reply_latency + "," + global_latency + "," + end2end;

                events.add(eventStr);
            }
        });
        sub.subscribe(feedBackTopic, timeout);

        HincMessage queryMessage = new HincMessage(HincMessage.MESSAGE_TYPE.RPC_QUERY_SDGATEWAY_LOCAL, groupName, HincMessageTopic.getCollectorTopicBroadcast(groupName), feedBackTopic, "");
        pub.pushMessage(queryMessage);

        // wait for a few second
        try {
            System.out.println("Wait for " + timeout + " miliseconds ...........");
            Thread.sleep(timeout);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(eventFileName, true)))) {
            System.out.println("Now saving events to file: " + eventFileName);
            for (String s : events) {
                out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getName() {
        return groupName;
    }

    public List<HincMeta> getListOfHINCLocal() {
        return listOfHINCLocal;
    }

    public void SendControl(ControlPoint controlPoint) {
        // TODO: implement this
    }

}
