/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.API;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.common.metadata.HincMessage;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.messageInterface.SalsaMessageHandling;
import sinc.hinc.global.management.CommunicationManager;
import sinc.hinc.model.CloudServices.CloudProvider;
import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.VirtualComputingResource.Capability.Capability;
import sinc.hinc.model.VirtualComputingResource.Capability.CapabilityType;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;
import sinc.hinc.model.VirtualNetworkResource.VNF;
import sinc.hinc.repository.DAO.orientDB.SoftwareDefinedGatewayDAO;
import sinc.hinc.model.API.ResourcesManagementAPI;

/**
 *
 * @author hungld
 */
@Service
//@Path("/")
public class ResourcesManagementAPIImpl implements ResourcesManagementAPI {

    CommunicationManager comMng = getCommunicationManager();
    static Logger logger = LoggerFactory.getLogger("HINC");
    List<HincLocalMeta> listOfHINCLocal = new ArrayList<>();


    public ResourcesManagementAPIImpl() {
    }

    public CommunicationManager getCommunicationManager() {
        if (comMng == null) {
            this.comMng = new CommunicationManager(HincConfiguration.getGroupName(), HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
        }
        return this.comMng;
    }

    @Override
    public List<SoftwareDefinedGateway> querySoftwareDefinedGateways(int timeout, String hincUUID) {
        logger.debug("Start broadcasting the query...");
        File dir = new File("log/queries");
        dir.mkdirs();
        logger.debug("Data is stored in: " + dir.getAbsolutePath());

        final List<String> events = new LinkedList<>();
        final List<SoftwareDefinedGateway> result = new ArrayList<>();

        String feedBackTopic = HincMessageTopic.getTemporaryTopic();

        final long timeStamp1 = (new Date()).getTime();
        String eventFileName = "log/queries/" + timeStamp1 + ".event";

        getCommunicationManager();
        if (hincUUID != null && !hincUUID.isEmpty()) {
            logger.debug("Trying to query HINC Local with ID: " + hincUUID);
            String gatewayInJson = comMng.synFunctionCallUnicast(hincUUID, HincMessage.MESSAGE_TYPE.RPC_QUERY_SDGATEWAY_LOCAL);
            result.add(SoftwareDefinedGateway.fromJson(gatewayInJson));
        } else {
            comMng.synFunctionCallBroadcast(timeout, feedBackTopic, HincMessage.MESSAGE_TYPE.RPC_QUERY_SDGATEWAY_LOCAL, HincMessageTopic.getCollectorTopicBroadcast(HincConfiguration.getGroupName()), new SalsaMessageHandling() {
                long latestTime = 0;
                long quantity = 0;
                long currentSum = 0;

                @Override
                public void handleMessage(HincMessage message) {
                    Long timeStamp5 = (new Date()).getTime();
                    logger.debug("Get a response message from " + message.getFromSalsa());
                    SoftwareDefinedGateway gw = SoftwareDefinedGateway.fromJson(message.getPayload());
                    if (gw == null) {
                        logger.debug("Payload is null, or cannot be converted");
                        return;
                    }
                    SoftwareDefinedGatewayDAO gwDAO = new SoftwareDefinedGatewayDAO();
                    gwDAO.save(gw);
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
                    System.out.println("Event is log: " + eventStr);
                    events.add(eventStr);
                }
            });
        }

        // wait for a few second
        try {
            logger.debug("Wait for " + timeout + " miliseconds ...........");
            Thread.sleep(timeout);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(eventFileName, true)))) {
            logger.debug("Now saving events to file: " + eventFileName);
            for (String s : events) {
                out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<DataPoint> queryDataPoint(int timeout, String hincUUID) {
        List<SoftwareDefinedGateway> gateways = querySoftwareDefinedGateways(timeout, hincUUID);
        List<DataPoint> datapoints = new ArrayList<>();
        for (SoftwareDefinedGateway gw : gateways) {
            for (Capability capa : gw.getDataPoints()) {
                if (capa.getCapabilityType() == CapabilityType.DataPoint) {
                    datapoints.add((DataPoint) capa);
                }
            }
        }
        return datapoints;
    }

    @Override
    public List<ControlPoint> queryControlPoint(int timeout, String hincUUID) {
        List<SoftwareDefinedGateway> gateways = querySoftwareDefinedGateways(timeout, hincUUID);
        List<ControlPoint> controlPoints = new ArrayList<>();
        for (SoftwareDefinedGateway gw : gateways) {
            for (Capability capa : gw.getControlPoints()) {
                if (capa.getCapabilityType() == CapabilityType.ControlPoint) {
                    controlPoints.add((ControlPoint) capa);
                }
            }
        }
        return controlPoints;
    }

    @Override
    public List<CloudConnectivity> queryConnectivity(int timeout, String hincUUID) {
        List<SoftwareDefinedGateway> gateways = querySoftwareDefinedGateways(timeout, hincUUID);
        List<CloudConnectivity> connectivity = new ArrayList<>();
        for (SoftwareDefinedGateway gw : gateways) {
            for (Capability capa : gw.getConnectivity()) {
                if (capa.getCapabilityType() == CapabilityType.CloudConnectivity) {
                    connectivity.add((CloudConnectivity) capa);
                }
            }
        }
        return connectivity;
    }

    @Override
    public List<NetworkFunctionService> queryNetworkService(int timeout, String hincUUID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<VNF> queryVNF(int timeout, String hincUUID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CloudService> queryCloudServices(int timeout, String hincUUID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CloudProvider> queryCloudProviders(int timeout, String hincUUID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendControl(UriInfo context, String providerUUID, String controlAction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
