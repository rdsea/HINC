/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import sinc.hinc.communication.factory.MessageClientFactory;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import sinc.hinc.abstraction.ResourceDriver.ProviderListenerAdaptor;
import sinc.hinc.abstraction.transformer.IoTUnitTransformer;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.processing.HINCMessageListener;
import sinc.hinc.local.messageHandlers.HandleControl;
import sinc.hinc.local.messageHandlers.HandleQueryIoTUnit;
import sinc.hinc.local.messageHandlers.HandleQueryVNF;
import sinc.hinc.local.messageHandlers.HandleSyn;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.repository.DAO.orientDB.DatabaseUtils;
import sinc.hinc.repository.DAO.orientDB.IoTUnitDAO;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.abstraction.ResourceDriver.ServiceDetector;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.local.messageHandlers.HandleQueryProviders;
import sinc.hinc.local.messageHandlers.HandleQueryService;
import sinc.hinc.local.messageHandlers.HandleUpdateInfobase;
import sinc.hinc.local.messageHandlers.HandleUpdateIoTUnit;
import sinc.hinc.model.API.WrapperMicroserviceArtifact;
import sinc.hinc.model.SoftwareArtifact.MicroserviceArtifact;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

/**
 *
 * @author hungld, linhsolar
 */
public class Main {

    static Logger logger = HincConfiguration.getLogger();
    static final MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
    static HINCMessageListener LISTENER = new HINCMessageListener(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
    //TODO: we will use a parameter 
    static final String DEFAULT_SOURCE_SETTINGS = "./sources.conf";
    static PluginRegistry pluginReg = new PluginRegistry();

    /**
     * Below is some metadata of the Local Management Service
     */
    String uuid;
    String ip;
    static int globalInterval;
    //Any other meta data of the environment where collector is deployed e.g. machine name, uname -a, cpu, max ram.     
    Map<String, String> meta;
    //a list of adaptors, currently just a short string for an adapter name.
    static List<String> enabledAdaptors = new ArrayList<>();

    private static void init() {
        globalInterval = Integer.parseInt(PropertiesManager.getParameter("global.interval", DEFAULT_SOURCE_SETTINGS));
        String enables = PropertiesManager.getParameter("global.enable", DEFAULT_SOURCE_SETTINGS);

        logger.debug("Enabled adaptors are: " + enables + ". Slit it: " + Arrays.toString(enables.split(",")));

        for (String s : enables.split(",")) {
            enabledAdaptors.add(s.trim());
            logger.debug("Enabled adaptor: " + s);
        }
    }

    public static void listen() {
        logger.info("Start to listen to the device changes ...");
        for (ProviderListenerAdaptor observer : pluginReg.getListeners()) {
            if (enabledAdaptors.contains(observer.getName().trim())) {
                String aName = observer.getName();
                logger.info("Now run the listener: " + aName);
                IoTUnitTransformer unitTrans = pluginReg.getIoTUnitTranformerByName(aName);
                observer.listen(PropertiesManager.getSettings(aName, DEFAULT_SOURCE_SETTINGS), unitTrans, new IoTUnitUpdateProcessor(HincConfiguration.getMyUUID()));
            }
        }
    }

    public static void detectServices() {
        logger.info("Start to detect services may running ... Total scanned adaptor: " + pluginReg.getServiceDetectors().size());
        WrapperMicroserviceArtifact wrapper = new WrapperMicroserviceArtifact();

        String enabled = "";
        for (String s : enabledAdaptors) {
            enabled += s + ",";
        }
        logger.debug("Enabled adaptors are : " + enabled);

        for (ServiceDetector detector : pluginReg.getServiceDetectors()) {
            logger.debug("Detecting: " + detector.getName() + " among enabled: " + enabledAdaptors);
            if (enabledAdaptors.contains(detector.getName().trim())) {
                logger.info("Detecting service : " + detector.getName());
                MicroserviceArtifact mService = detector.detect(PropertiesManager.getSettings(detector.getName().trim(), DEFAULT_SOURCE_SETTINGS));
                mService.setHostID(HincConfiguration.getMyUUID());
                // save to DB
                AbstractDAO<MicroserviceArtifact> serviceDAO = new AbstractDAO(MicroserviceArtifact.class);
                serviceDAO.save(mService);
                wrapper.getmServices().add(mService);
            }
        }
        // send message to the group topic, message type: UPDATE_INFORMATION
        if (!wrapper.getmServices().isEmpty()) {
            String groupTopic = HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName());
            HincMessage updateMsg = new HincMessage(HINCMessageType.UPDATE_INFORMATION_MICRO_SERVICE.toString(), HincConfiguration.getMyUUID(), groupTopic, "", wrapper.toJson());
            FACTORY.getMessagePublisher().pushMessage(updateMsg);
        }
    }
    
    //Current it is just a sequence of calls, it is not good for performance
    //TODO: change it
    public static void scanOnce() throws InterruptedException {

        logger.debug("We have {} adaptor... now will check each one", pluginReg.getAdaptors().size());
        Set<IoTUnit> units = new HashSet<>();
        Set<ResourcesProvider> resourceProviders = new HashSet<>();
        for (ProviderQueryAdaptor adaptor : pluginReg.getAdaptors()) {
            if (enabledAdaptors.contains(adaptor.getName())) {
                String aName = adaptor.getName();
                logger.info("Querying provider: " + aName);

                IoTUnitTransformer unitTrans = pluginReg.getIoTUnitTranformerByName(aName);

                Collection<Object> domains = adaptor.getItems(PropertiesManager.getSettings(aName, DEFAULT_SOURCE_SETTINGS));
                //simple check
                if (domains == null)
                    continue;
                logger.debug("We will check {} items.." + domains.size());
                for (Object domain : domains) {
                    logger.debug("Checking item: " + domain.toString());
                    IoTUnit unit = unitTrans.translateIoTUnit(domain);
                    unit.setHincID(HincConfiguration.getMyUUID());
                    logger.debug("HINC UUID: " + unit.getHincID() + ", unit  uuid: " + unit.getUuid());
                    units.add(unit);
                }
                // check provider APIs
                try{
                    ResourcesProvider rp = adaptor.getProviderAPI(PropertiesManager.getSettings(aName, DEFAULT_SOURCE_SETTINGS));
                    resourceProviders.add(rp);
                }catch(UnsupportedOperationException e){
                    logger.warn(e.getMessage());
                }

                Thread.sleep(1000); // a short break between sources
            }
        }

        System.out.println("Transforming IoTUnit is done, number of IoT Units: " + units.size());
        IoTUnitDAO unitDAO = new IoTUnitDAO();
        unitDAO.saveAll(units);
        AbstractDAO<ResourcesProvider> rpDAO = new AbstractDAO<>(ResourcesProvider.class);
        rpDAO.saveAll(resourceProviders);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting HINC Local Management Service...");
        init();
        Long time1 = (new Date()).getTime();
        DatabaseUtils.initDB();

        Long time2 = (new Date()).getTime();
        System.out.println("Time to bring up DB is: " + (time2 - time1) / 1000);

        /**
         * ************************
         * HINC listens to some queue channels to answer the query. Because the
         * limitation of connection to the queue, each HINC local subscribes only
         * to public topic at the moment. ************************
         */
        String groupTopic = HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName());
//        String privaTopic = HincMessageTopic.getHINCPrivateTopic(HincConfiguration.getMyUUID());

        LISTENER.addListener(groupTopic, HINCMessageType.SYN_REQUEST.toString(), new HandleSyn());
        LISTENER.addListener(groupTopic, HINCMessageType.QUERY_IOT_UNIT.toString(), new HandleQueryIoTUnit());
        LISTENER.addListener(groupTopic, HINCMessageType.QUERY_IOT_PROVIDERS.toString(), new HandleQueryProviders());
//        LISTENER.addListener(privaTopic, HINCMessageType.QUERY_GATEWAY_LOCAL.toString(), new HandleQueryIoTUnit());

        LISTENER.addListener(groupTopic, HINCMessageType.QUERY_MICRO_SERVICE_LOCAL.toString(), new HandleQueryService());
//        LISTENER.addListener(privaTopic, HINCMessageType.QUERY_MICRO_SERVICE_LOCAL.toString(), new HandleQueryService());

        LISTENER.addListener(groupTopic, HINCMessageType.QUERY_NFV_LOCAL.toString(), new HandleQueryVNF());
//        LISTENER.addListener(privaTopic, HINCMessageType.QUERY_NFV_LOCAL.toString(), new HandleQueryVNF());

        LISTENER.addListener(groupTopic, HINCMessageType.CONTROL.toString(), new HandleControl());
//        LISTENER.addListener(privaTopic, HINCMessageType.CONTROL.toString(), new HandleControl());

        LISTENER.addListener(groupTopic, HINCMessageType.UPDATE_INFO_BASE.toString(), new HandleUpdateInfobase());
        
        LISTENER.addListener(groupTopic, HINCMessageType.PROVIDER_UPDATE_IOT_UNIT.toString(), new HandleUpdateIoTUnit());

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

        listen();
        detectServices();
        //Linh Note
        //TODO: a better way to schedule scan
        while (true) {
            
            scanOnce();

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

        // try to register itself
        HincMessage synMsg = new HincMessage(HINCMessageType.SYN_REPLY.toString(), HincConfiguration.getMyUUID(), HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName()), "", HincConfiguration.getLocalMeta().toJson());
        FACTORY.getMessagePublisher().pushMessage(synMsg);

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
