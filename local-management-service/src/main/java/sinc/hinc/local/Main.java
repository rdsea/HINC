/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import org.slf4j.Logger;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.local.communication.AdaptorCommunicationManager;
import sinc.hinc.local.communication.LocalCommunicationManager;
import sinc.hinc.local.plugin.Adaptor;
import sinc.hinc.local.plugin.AdaptorManager;
import sinc.hinc.repository.DAO.orientDB.DatabaseUtils;
/**
 *
 * @author hungld, linhsolar
 */
public class Main {

    static Logger logger = HincConfiguration.getLogger();
    public static int globalInterval = Integer.parseInt(PropertiesManager.getParameter("global.interval", "./sources.conf"));
    public static String globalInputExchange = "hinc_global_input";


    public static void main(String[] args) throws Exception {
        logger.info("Starting HINC Local Management Service...");
        DatabaseUtils.initDB();
        logger.info("DB initialized");

        LocalCommunicationManager.initialize(
                HincConfiguration.getBroker(),
                HincConfiguration.getGroupName(),
                HincConfiguration.getMyUUID(),
                globalInputExchange);
        logger.info("initialized hinc communication manager");

        AdaptorCommunicationManager.initialize(
                HincConfiguration.getBroker(),
                HincConfiguration.getGroupName(),
                HincConfiguration.getMyUUID()
        );
        logger.info("initialized adaptor communication manager");

        /**
         * ************************
         * HINC start threads to collect information from providers.
         * ************************ Check the registry, for all the transformer,
         * call the appropriate adaptors, save information to a
         * SoftwareDefinedGateway
         */
        while (true) {
            AdaptorManager.getInstance().scanAll();
            // Process interval 
            try {
                System.out.println("Sleeping " + globalInterval + " before next query.");
                Thread.sleep(globalInterval * 1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
