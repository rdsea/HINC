/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.API;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.processing.HINCMessageSender;
import sinc.hinc.model.API.ResourcesManagementAPI;
import sinc.hinc.model.CloudServices.CloudProvider;
import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.model.VirtualNetworkResource.NetworkService;
import sinc.hinc.model.VirtualNetworkResource.VNF;
import sinc.hinc.repository.DAO.orientDB.SoftwareDefinedGatewayDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.model.VirtualComputingResource.Capabilities.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capability;
import sinc.hinc.repository.DAO.orientDB.DatabaseUtils;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;
import sinc.hinc.model.slice.AppSlice;
import sinc.hinc.model.slice.InfrastructureSlice;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

/**
 * @author hungld
 */
@Service
//@Path("/")
public class ResourcesManagementAPIImpl implements ResourcesManagementAPI {

    static Logger logger = LoggerFactory.getLogger("HINC");
    HINCMessageSender comMng = getCommunicationManager();
    List<HincLocalMeta> listOfHINCLocal = new ArrayList<>();

    {
        // check database, if not exist then create
        DatabaseUtils.initDB();
    }

    public ResourcesManagementAPIImpl() {
    }

    public HINCMessageSender getCommunicationManager() {
        if (comMng == null) {
            this.comMng = new HINCMessageSender(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
        }
        return this.comMng;
    }

    @Override
    public Set<SoftwareDefinedGateway> querySoftwareDefinedGateways(int timeout, String hincUUID, String rescan) {
        logger.debug("Start broadcasting the query...");
        File dir = new File("logs/queries");
        dir.mkdirs();
        logger.debug("Data is stored in: " + dir.getAbsolutePath());

        final List<String> events = new LinkedList<>();
        final Set<SoftwareDefinedGateway> result = new HashSet<>();

        String feedBackTopic = HincMessageTopic.getTemporaryTopic();

        final long timeStamp1 = (new Date()).getTime();
        String eventFileName = "logs/queries/" + timeStamp1 + ".event";
        String payload = "";
        if (rescan.equals("true")) {
            payload = "rescan";
        }
        if (hincUUID != null && !hincUUID.isEmpty() && !hincUUID.trim().equals("null")) {
            logger.debug("Trying to query HINC Local with ID: " + hincUUID);
            String gatewayInJson = comMng.synCall(new HincMessage(HINCMessageType.RPC_QUERY_SDGATEWAY_LOCAL.toString(), HincConfiguration.getMyUUID(), HincMessageTopic.getHINCPrivateTopic(hincUUID), feedBackTopic, payload));

            result.add(SoftwareDefinedGateway.fromJson(gatewayInJson));
        } else {
            HincMessage queryMessage = new HincMessage(HINCMessageType.RPC_QUERY_SDGATEWAY_LOCAL.toString(), HincConfiguration.getMyUUID(), HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName()), feedBackTopic, payload);
            comMng.asynCall(timeout, queryMessage, new HINCMessageHander() {
                long latestTime = 0;
                long quantity = 0;
                long currentSum = 0;

                @Override
                public HincMessage handleMessage(HincMessage message) {
                    Long timeStamp5 = (new Date()).getTime();
                    logger.debug("Get a response message from " + message.getSenderID());

                    ObjectMapper mapper = new ObjectMapper();

                    try {
                        List<SoftwareDefinedGateway> gws;
                        gws = mapper.readValue(message.getPayload(), new TypeReference<List<SoftwareDefinedGateway>>() {
                        });

                        for (SoftwareDefinedGateway gw : gws) {
                            if (gw == null) {
                                logger.debug("Payload is null, or cannot be converted");
                                return null;
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
                    } catch (IOException ex) {
                        logger.error("Error when unmarshall the reply..." + ex);
                        ex.printStackTrace();
                    }
                    return null;
                }
            });
        }

        // wait for a few second
        try {
            logger.debug("Wait for " + timeout + " miliseconds for subscription threads finish ...........");
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
    public Collection<DataPoint> queryDataPoint(int timeout, String hincUUID) {
        if (timeout == 0) {
            AbstractDAO<DataPoint> dao = new AbstractDAO<>(DataPoint.class);
            return dao.readAll();
        }
        Set<SoftwareDefinedGateway> gateways = querySoftwareDefinedGateways(timeout, hincUUID, "false");
        List<DataPoint> datapoints = new ArrayList<>();
        for (SoftwareDefinedGateway gw : gateways) {
            for (Capability capa : gw.getDataPoints()) {
                datapoints.add((DataPoint) capa);
            }
        }
        return datapoints;
    }

    @Override
    public List<ControlPoint> queryControlPoint(int timeout, String hincUUID) {
        if (timeout == 0) {
            AbstractDAO<ControlPoint> dao = new AbstractDAO<>(ControlPoint.class);
            return dao.readAll();
        }
        Set<SoftwareDefinedGateway> gateways = querySoftwareDefinedGateways(timeout, hincUUID, "false");
        List<ControlPoint> controlPoints = new ArrayList<>();
        for (SoftwareDefinedGateway gw : gateways) {
            for (Capability capa : gw.getControlPoints()) {
                controlPoints.add((ControlPoint) capa);
            }
        }
        return controlPoints;
    }

    @Override
    public List<CloudConnectivity> queryConnectivity(int timeout, String hincUUID) {
        if (timeout == 0) {
            AbstractDAO<CloudConnectivity> dao = new AbstractDAO<>(CloudConnectivity.class);
            return dao.readAll();
        }
        Set<SoftwareDefinedGateway> gateways = querySoftwareDefinedGateways(timeout, hincUUID, "false");
        List<CloudConnectivity> connectivity = new ArrayList<>();
        for (SoftwareDefinedGateway gw : gateways) {
            for (Capability capa : gw.getConnectivity()) {
                connectivity.add((CloudConnectivity) capa);

            }
        }
        return connectivity;
    }

    List<NetworkService> networkServiceMock;

    private void generateNetworkMock() { // create MOCK
        networkServiceMock = new ArrayList<>();
        networkServiceMock.add(new NetworkService(UUID.randomUUID().toString(), "iot.eclipse.org", NetworkService.NetworkServiceType.BROKER_MQTT, new AccessPoint("tcp://iot.eclipse.org:1883")));
        networkServiceMock.add(new NetworkService(UUID.randomUUID().toString(), "test.mosquitto.org", NetworkService.NetworkServiceType.BROKER_MQTT, new AccessPoint("tcp://test.mosquitto.org:1883")));
        networkServiceMock.add(new NetworkService(UUID.randomUUID().toString(), "broker.hivemq.com", NetworkService.NetworkServiceType.BROKER_MQTT, new AccessPoint("tcp://broker.hivemq.com:1883")));
        networkServiceMock.add(new NetworkService(UUID.randomUUID().toString(), "californium.eclipse.org", NetworkService.NetworkServiceType.BROKER_COAP, new AccessPoint("coap://californium.eclipse.org:5683/")));
    }

    // TODO: really implement network service querying instead of using MOCK info
    @Override
    public Collection<NetworkService> queryNetworkService(int timeout, String hincUUID) {
        if (networkServiceMock == null) {
            generateNetworkMock();
        }
        return networkServiceMock;
//        AbstractDAO<NetworkService> dao = new AbstractDAO<>(NetworkService.class);
//        return dao.readAll();
    }

    @Override
    public String addNetworkService(NetworkService networkService) {
        logger.debug("Saving a Netwetworkork Service: " + networkService.toJson());
        AbstractDAO<NetworkService> dao = new AbstractDAO<>(NetworkService.class);
        return dao.save(networkService).toString();
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
    public String sendControl(String gatewayid, String resourceid, String actionName, String param) {
        String controlPointUUID = gatewayid + "/" + resourceid + "/" + actionName;
        HincMessage controlPointRequest = new HincMessage(HINCMessageType.CONTROL.toString(), HincConfiguration.getMyUUID(), HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName()), HincMessageTopic.getTemporaryTopic(), controlPointUUID);
        controlPointRequest.hasExtra("param", param);
        return comMng.synCall(controlPointRequest);
    }

    @Override
    public AppSlice configureDataSlide(String datapointID, String networkID, String cloudServiceID) {

        AbstractDAO<DataPoint> dao1 = new AbstractDAO<>(DataPoint.class);
        AbstractDAO<NetworkService> dao2 = new AbstractDAO<>(NetworkService.class);
        AbstractDAO<CloudService> dao3 = new AbstractDAO<>(CloudService.class);
        AbstractDAO<ControlPoint> dao4 = new AbstractDAO<>(ControlPoint.class);

        DataPoint dataPoint = dao1.read(datapointID);
        NetworkService network = dao2.read(networkID);
        ControlPoint control = getControlPointConnectToNetwork(datapointID, network);
        if (control != null) {
            System.out.println("Found a control point to connect datapoint: " + dataPoint.getName() + " to the network" + network.getName());
            String r = sendControl(dataPoint.getGatewayID(), dataPoint.getResourceID(), control.getName(), network.getAccessPoint().getEndpoint());
            System.out.println("The slide configuration return result: " + r);
        }
//        CloudService cloud = dao3.read(cloudServiceID);
        return new AppSlice(dataPoint, network, null);
    }

    public List<ControlPoint> getControlOfDatapoint(String datapointID) {
        AbstractDAO<ControlPoint> dao4 = new AbstractDAO<>(ControlPoint.class);
        return dao4.readWithCondition("uuid like '" + datapointID.trim() + "%'");
    }

    public List<ControlPoint> getControlPointOfType(String datapointID, ControlPoint.ControlType type) {
        List<ControlPoint> controls = getControlOfDatapoint(datapointID);
        List<ControlPoint> connectionControl = new ArrayList<>();
        for (ControlPoint cp : controls) {
            if (cp.getControlType().equals(type)) {
                connectionControl.add(cp);
            }
        }
        return connectionControl;
    }

    public ControlPoint getControlPointConnectToNetwork(String datapoinID, NetworkService network) {
        List<ControlPoint> controls = getControlPointOfType(datapoinID, ControlPoint.ControlType.CONNECT_TO_NETWORK);
        for (ControlPoint cp : controls) {
            if (cp.getCondition().equals(network.getType().toString())) {
                return cp;
            }
        }
        return null;
    }

    @Override

    public InfrastructureSlice configureInfrastructureSlide(String gatewayID, String networkID, String cloudServiceID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
