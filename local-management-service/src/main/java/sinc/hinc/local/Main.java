/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import sinc.hinc.common.utils.IPLocationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import sinc.hinc.communication.messageInterface.MessageClientFactory;
import sinc.hinc.repository.DAO.orientDB.OrientDBConnector;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import org.slf4j.Logger;
import sinc.hinc.common.metadata.InfoSourceSettings;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.repository.DAO.orientDB.DatabaseUtils;

/**
 *
 * @author hungld
 */
public class Main {

    static Logger logger = HincConfiguration.getLogger();
    static final MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());

    /**
     * Below is some metadata of the Local Management Service
     */
    String uuid;
    String ip;
    //Any other meta data of the environment where collector is deployed e.g. machine name, uname -a, cpu, max ram.     
    Map<String, String> meta;
    static InfoSourceSettings settings;

    private static boolean hasSettings() {
        System.out.println("Loadding settings file...");
        settings = InfoSourceSettings.loadDefaultFile();
        System.out.println("Load resource file done !");
        if (settings == null || settings.getSource().isEmpty()) {
            logger.error("ERROR: No source information found. Please check configuration file.");
            System.out.println("ERROR: No source information found. Please check configuration file.");
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting collector...");
        Long time1 = (new Date()).getTime();
        DatabaseUtils.initDB();
        
        Long time2 = (new Date()).getTime();
        System.out.println("Time to bring up DB is: " + (time2 - time1) / 1000);

        /**
         * ************************
         * HINC listens to some queue channels to answer the query. ************************
         */
        QueueListener.listenSynMessage();
        QueueListener.listenQueryMessage();

        /**
         * ************************
         * HINC start threads to collect information from providers. ************************
         */
        Long time3 = (new Date()).getTime();
        logger.info("Local management service startup in " + ((double) time3 - (double) time2) / 1000 + " seconds, uuid: " + HincConfiguration.getMyUUID());
        logger.info("Starting to interact with providers ...");

        if (hasSettings()) {
            for (InfoSourceSettings.InfoSource source : settings.getSource()) {
                switch (source.getType()) {
                    case IoT: {
                        CollectResourceIoT collectorThread = new CollectResourceIoT(source);
                        collectorThread.run();
                        break;
                    }
                    case Cloud: {
                        CollectResourceCloud collectorThread = new CollectResourceCloud(source);
                        collectorThread.run();
                        break;
                    }
                    case Network: {
                        CollectResourceNetwork collectorThread = new CollectResourceNetwork(source);
                        collectorThread.run();
                        break;
                    }
                }
                Thread.sleep(1000); // a short break between sources
            }
        } else {
            System.out.println("Do not query any provider. Configuration file not found.");
        }
    }

   

}
