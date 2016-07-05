/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import java.util.Collection;
import sinc.hinc.communication.factory.MessageClientFactory;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import sinc.hinc.abstraction.ResourceDriver.ProviderAdaptor;
import sinc.hinc.abstraction.transformer.ConnectivityTransformater;
import sinc.hinc.abstraction.transformer.ControlPointTransformer;
import sinc.hinc.abstraction.transformer.DataPointTransformer;
import sinc.hinc.abstraction.transformer.ExecutionEnvironmentTransformer;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.common.utils.HincUtils;
import sinc.hinc.communication.processing.HINCMessageListener;
import sinc.hinc.local.messageHandlers.HandleQueryGateway;
import sinc.hinc.local.messageHandlers.HandleQueryVNF;
import sinc.hinc.local.messageHandlers.HandleSyn;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.repository.DAO.orientDB.DatabaseUtils;
import sinc.hinc.repository.DAO.orientDB.SoftwareDefinedGatewayDAO;

/**
 *
 * @author hungld
 */
public class Main {

    static Logger logger = HincConfiguration.getLogger();
    static final MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
    static HINCMessageListener LISTENER = new HINCMessageListener(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
    static final String DEFAULT_SOURCE_SETTINGS = "./sources.conf";
    /**
     * Below is some metadata of the Local Management Service
     */
    String uuid;
    String ip;
    static int globalInterval;
    //Any other meta data of the environment where collector is deployed e.g. machine name, uname -a, cpu, max ram.     
    Map<String, String> meta;
    
    {
        globalInterval = Integer.parseInt(PropertiesManager.getParameter("interval", DEFAULT_SOURCE_SETTINGS));
    }



    public static void main(String[] args) throws Exception {
        System.out.println("Starting HINC Local Management Service...");
        Long time1 = (new Date()).getTime();
        DatabaseUtils.initDB();

        Long time2 = (new Date()).getTime();
        System.out.println("Time to bring up DB is: " + (time2 - time1) / 1000);

        /**
         * ************************
         * HINC listens to some queue channels to answer the query.
         * ************************
         */
        String groupTopic = HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName());
        String privateTopic = HincMessageTopic.getHINCPrivateTopic(HincConfiguration.getMyUUID());

        LISTENER.addListener(groupTopic, HINCMessageType.SYN_REQUEST.toString(), new HandleSyn());
        LISTENER.addListener(groupTopic, HINCMessageType.RPC_QUERY_SDGATEWAY_LOCAL.toString(), new HandleQueryGateway());
        LISTENER.addListener(privateTopic, HINCMessageType.RPC_QUERY_SDGATEWAY_LOCAL.toString(), new HandleQueryGateway());
        LISTENER.addListener(groupTopic, HINCMessageType.RPC_QUERY_NFV_LOCAL.toString(), new HandleQueryVNF());
        LISTENER.addListener(privateTopic, HINCMessageType.RPC_QUERY_NFV_LOCAL.toString(), new HandleQueryVNF());

        LISTENER.listen();

        /**
         * ************************
         * HINC start threads to collect information from providers.
         * ************************ Check the registry, for all the transformer,
         * call the appropriate adaptors, save information to a
         * SoftwareDefinedGateway
         */
        Long time3 = (new Date()).getTime();
        logger.info("Local management service startup in " + ((double) time3 - (double) time2) / 1000 + " seconds, uuid: " + HincConfiguration.getMyUUID());
        logger.info("Starting to interact with providers ...");

        PluginRegistry pluginReg = new PluginRegistry();

        SoftwareDefinedGateway gw = new SoftwareDefinedGateway();
        gw.setUuid(HincConfiguration.getMyUUID());
        gw.setName(HincUtils.getHostName());
        
        
        while (true) {
            logger.debug("We have {} adaptor... now will check each one", pluginReg.getAdaptors().size());
            for (ProviderAdaptor adaptor : pluginReg.getAdaptors()) {
                String aName = adaptor.getName();
                logger.info("Querying provider: " + aName);
                DataPointTransformer dpt = pluginReg.getDatapointTransformerByName(aName);
                ControlPointTransformer cpt = pluginReg.getControlpointTransformerByName(aName);
                ExecutionEnvironmentTransformer eet = pluginReg.getExecutionEnvTransformerByName(aName);
                ConnectivityTransformater cct = pluginReg.getConnectivityTransformerByName(aName);

                Collection<Object> domains = adaptor.getItems(PropertiesManager.getSettings(aName, DEFAULT_SOURCE_SETTINGS));
                logger.debug("We will check {} items.." + domains.size());
                for (Object domain : domains) {
                    logger.debug("Checking item: " + domain.toString());
                    if (dpt != null) {
                        logger.debug("Datapoint translation is available, transforming datapoints...");
                        DataPoint dp = dpt.updateDataPoint(domain);
                        logger.debug("Got a datapoint: " + dp.getUuid() + ".." + dp.getName());
                        gw.hasCapability(dpt.updateDataPoint(domain));
                    } else {
                        logger.debug("Datapoint translation is NOT available !");
                    }
                    if (cpt != null) {
                        gw.hasCapabilities(cpt.updateControlPoint(domain));
                    } else {
                        logger.debug("Controlpoint translation is NOT available !");
                    }
                    if (eet != null) {
                        gw.hasCapability(eet.updateExecutionEnvironment(domain));
                    }
                    if (cct != null) {
                        gw.hasCapability(cct.updateCloudConnectivity(domain));
                    }
                }
                Thread.sleep(1000); // a short break between sources
            }

            System.out.println("Transform GW done, number of datapoint: " + gw.getDataPoints().size() + ", controlpoint:" + gw.getControlPoints().size());
            SoftwareDefinedGatewayDAO gwDAO = new SoftwareDefinedGatewayDAO();
            gwDAO.save(gw);

            // Process interval 
            int interval = globalInterval;
            // TODO: read local interval setting
            if (interval == 0) {
                System.out.println("Interval equals 0, query done!");
                break;
            } else if (interval > 0) {
                try {
                    System.out.println("Sleeping " + interval + " before next query.");
                    Thread.sleep(interval * 1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        // query 1 time or continuously
//        if (source.getInterval() == 0) {
//            System.out.println("Interval equals 0, query done!");
//            break;
//        } else if (source.getInterval() > 0) {
//            try {
//                System.out.println("Sleeping " + source.getInterval() + " before next query.");
//                Thread.sleep(source.getInterval() * 1000);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//        }
        /**
         * ************************
         * HINC start threads to collect information from providers.
         * ************************
         */
//        Long time3 = (new Date()).getTime();
//        logger.info("Local management service startup in " + ((double) time3 - (double) time2) / 1000 + " seconds, uuid: " + HincConfiguration.getMyUUID());
//        logger.info("Starting to interact with providers ...");
//
//        if (hasSettings()) {
//            for (InfoSourceSettings.InfoSource source : settings.getSource()) {
//                switch (source.getType()) {
//                    case IoT: {
//                        CollectResourceIoT collectorThread = new CollectResourceIoT(source);
//                        collectorThread.run();
//                        break;
//                    }
//                    case Cloud: {
//                        CollectResourceCloud collectorThread = new CollectResourceCloud(source);
//                        collectorThread.run();
//                        break;
//                    }
//                    case Network: {
//                        CollectResourceNetwork collectorThread = new CollectResourceNetwork(source);
//                        collectorThread.run();
//                        break;
//                    }
//                }
//                Thread.sleep(1000); // a short break between sources
//            }
//        } else {
//            System.out.println("Do not query any provider. Configuration file not found.");
//        }
    }

    public static HINCMessageListener getListener() {
        return LISTENER;
    }

}
