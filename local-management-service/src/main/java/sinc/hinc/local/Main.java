/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import org.slf4j.Logger;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.repository.DAO.orientDB.DatabaseUtils;

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
        DatabaseUtils.initDB();
        logger.info("DB initialized");


        /**
         * ************************
         * HINC start threads to collect information from providers.
         * ************************ Check the registry, for all the transformer,
         * call the appropriate adaptors, save information to a
         * SoftwareDefinedGateway
         */
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
    }
}
