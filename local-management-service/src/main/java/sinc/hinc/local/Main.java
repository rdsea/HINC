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
import sinc.hinc.communication.processing.HincMessage;

/**
 *
 * @author hungld
 */
public class Main {

    static Logger logger = HincConfiguration.getLogger();
    static final MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
    static HINCMessageListener LISTENER = new HINCMessageListener(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
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

    public static void scanOnce() throws InterruptedException {

        logger.debug("We have {} adaptor... now will check each one", pluginReg.getAdaptors().size());
        Set<IoTUnit> units = new HashSet<>();
        for (ProviderQueryAdaptor adaptor : pluginReg.getAdaptors()) {
            if (enabledAdaptors.contains(adaptor.getName())) {
                String aName = adaptor.getName();
                logger.info("Querying provider: " + aName);

                IoTUnitTransformer unitTrans = pluginReg.getIoTUnitTranformerByName(aName);

                Collection<Object> domains = adaptor.getItems(PropertiesManager.getSettings(aName, DEFAULT_SOURCE_SETTINGS));
                logger.debug("We will check {} items.." + domains.size());
                for (Object domain : domains) {
                    logger.debug("Checking item: " + domain.toString());
                    IoTUnit unit = unitTrans.translateIoTUnit(domain);
                    unit.setHincID(HincConfiguration.getMyUUID());
                    logger.debug("HINC UUID: " + unit.getHincID() + ", unit  uuid: " + unit.getUuid());
                    units.add(unit);
                }
                Thread.sleep(1000); // a short break between sources
            }
        }

        System.out.println("Transforming IoTUnit is done, number of IoT Units: " + units.size());
        IoTUnitDAO unitDAO = new IoTUnitDAO();
        unitDAO.saveAll(units);
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
         * HINC listens to some queue channels to answer the query.
         * ************************
         */
        String groupTopic = HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName());
        String privaTopic = HincMessageTopic.getHINCPrivateTopic(HincConfiguration.getMyUUID());

        LISTENER.addListener(groupTopic, HINCMessageType.SYN_REQUEST.toString(), new HandleSyn());
        LISTENER.addListener(groupTopic, HINCMessageType.RPC_QUERY_SDGATEWAY_LOCAL.toString(), new HandleQueryIoTUnit());
        LISTENER.addListener(privaTopic, HINCMessageType.RPC_QUERY_SDGATEWAY_LOCAL.toString(), new HandleQueryIoTUnit());
        LISTENER.addListener(groupTopic, HINCMessageType.RPC_QUERY_NFV_LOCAL.toString(), new HandleQueryVNF());
        LISTENER.addListener(privaTopic, HINCMessageType.RPC_QUERY_NFV_LOCAL.toString(), new HandleQueryVNF());

        LISTENER.addListener(groupTopic, HINCMessageType.CONTROL.toString(), new HandleControl());
        LISTENER.addListener(privaTopic, HINCMessageType.CONTROL.toString(), new HandleControl());

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
