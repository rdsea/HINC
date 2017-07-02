/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.messageHandlers;

import com.orientechnologies.orient.core.record.impl.ODocument;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.model.API.WrapperIoTUnit;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.repository.DAO.orientDB.IoTUnitDAO;

/**
 * This handler enable provider update IoT Unit to this HINC local via message
 * queue.
 *
 * This log performance with:
 *
 * - timestamp1: when devides change, - timestamp2: local recieve - timestamp3:
 * local save to DB completely and ready to send out.
 *
 * @author hungld
 */
public class HandleUpdateIoTUnit implements HINCMessageHander {

    static org.slf4j.Logger logger = HincConfiguration.getLogger();
    static int count = 0;

    @Override
    public HincMessage handleMessage(HincMessage message) {
        List<String> events = new LinkedList<>();
        Long st_local_get_msg = (new Date()).getTime();
        logger.debug("----> Get  message from " + message.getSenderID() + ". Msg count: " + ++count);
        WrapperIoTUnit wrapper = new WrapperIoTUnit(message.getPayload());
        logger.debug("Get the wrapper: " + wrapper.toJson());
        Set<IoTUnit> result_update = new HashSet<>();
        Set<IoTUnit> result_remove = new HashSet<>();
        for (IoTUnit unit : wrapper.getUnits()) {
            if (unit.getMeta() != null && unit.getMeta().get("remove").equals("true")) {
                result_remove.add(unit);
            } else {
                result_update.add(unit);
            }
        }
        logger.debug("HINC " + message.getSenderID() + " send: " + result_update.size() + " updated units, and " + result_remove.size() + " removed units.");
        IoTUnitDAO dao = new IoTUnitDAO();
        List<ODocument> odocs = dao.saveAll(result_update);
        for (IoTUnit unit : result_remove) {
            logger.debug("Removing item: " + unit.getUuid());
            dao.delete(unit);
        }
        logger.debug("Good, saving " + odocs.size() + " items, and delete " + result_remove.size() + " items");

        // ==== Record time for various experiments ===
        Long st_local_saved_data = (new Date()).getTime();

        if (message.getExtra() != null && !message.getExtra().isEmpty() && message.getExtra().containsKey("st_device_change")) {
            Long st_device_change = Long.parseLong(message.getExtra().get("st_device_change"));
            Long time_provider_to_local = st_local_get_msg - st_device_change;
            Long time_local_process = st_local_saved_data - st_local_get_msg;
            Long end2end = time_local_process + time_provider_to_local;

            String eventStr = message.getSenderID() + "," + st_device_change + "," + st_local_get_msg + ","
                    + st_local_saved_data + "," + time_provider_to_local + "," + time_local_process + "," + end2end + "\n";
            logger.debug("Event is log: " + eventStr);

            String logFile = "local.events.txt";
            try {
                logger.debug("Saving event to file");
                new File(logFile).createNewFile();
                Files.write(Paths.get(logFile), eventStr.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException ex) {
                logger.error("Cannot create event log file: " + logFile);
                ex.printStackTrace();
            } catch (Exception e) {
                logger.error("Some error happen: ", e);
                e.printStackTrace();
            }
            events.add(eventStr);
        } else {
            logger.error("Cannot detect timestamp in the HINC message, no event is recorded.");
        }

        return null;
    }

}
