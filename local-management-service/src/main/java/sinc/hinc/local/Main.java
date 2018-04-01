/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import org.slf4j.Logger;
import java.util.Date;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.processing.HINCMessageListener;
import sinc.hinc.local.messageHandlers.*;
import sinc.hinc.repository.DAO.orientDB.DatabaseUtils;
import sinc.hinc.communication.processing.HincMessage;

import static sinc.hinc.local.LocalManagementService.FACTORY;
import static sinc.hinc.local.LocalManagementService.LISTENER;
import static sinc.hinc.local.LocalManagementService.globalInterval;

/**
 *
 * @author hungld, linhsolar
 */
public class Main {

    static Logger logger = HincConfiguration.getLogger();

    public static void main(String[] args) throws Exception {
        logger.info("Starting HINC Local Management Service...");
        LocalManagementService localManagementService = new LocalManagementService();

        Long time1 = (new Date()).getTime();
        DatabaseUtils.initDB();
        Long time2 = (new Date()).getTime();
        logger.info("Time to bring up DB is: " + (time2 - time1) / 1000);

        /**
         * ************************
         * HINC listens to some queue channels to answer the query. Because the
         * limitation of connection to the queue, each HINC local subscribes only
         * to public topic at the moment. ************************
         */
        String groupTopic = HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName());
        LISTENER.addListener(groupTopic, HINCMessageType.SYN_REQUEST.toString(), new HandleSyn());
        LISTENER.addListener(groupTopic, HINCMessageType.QUERY_IOT_UNIT.toString(), new HandleQueryIoTUnit());
        LISTENER.addListener(groupTopic, HINCMessageType.QUERY_IOT_PROVIDERS.toString(), new HandleQueryProviders());
        LISTENER.addListener(groupTopic, HINCMessageType.QUERY_MICRO_SERVICE_LOCAL.toString(), new HandleQueryService());
        LISTENER.addListener(groupTopic, HINCMessageType.QUERY_NFV_LOCAL.toString(), new HandleQueryVNF());
        LISTENER.addListener(groupTopic, HINCMessageType.CONTROL.toString(), new HandleControl());
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

        while (true) {
            
            LocalManagementService.scanAdaptors();

            // Process interval 
            int interval = globalInterval;
            // TODO: read local interval setting
            if (interval == 0) {
                System.out.println("Interval equals 0, query done!");
                break;
            } else if (interval > 0) {
                try {
                    System.out.println("Sleeping " + interval + " before next query.");
                    Thread.sleep(interval * 3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        // try to register itself
        HincMessage synMsg = new HincMessage(HINCMessageType.SYN_REPLY.toString(), HincConfiguration.getMyUUID(), HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName()), "", HincConfiguration.getLocalMeta().toJson());
        FACTORY.getMessagePublisher().pushMessage(synMsg);
    }

    public static HINCMessageListener getListener() {
        return LISTENER;
    }

}
