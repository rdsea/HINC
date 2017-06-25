/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import sinc.hinc.abstraction.ResourceDriver.IoTUnitProcessor;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.factory.MessageClientFactory;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.repository.DAO.orientDB.IoTUnitDAO;

/**
 *
 * @author hungld
 */
public class IoTUnitUpdateProcessor implements IoTUnitProcessor {

    String hincUUID;

    public IoTUnitUpdateProcessor(String hincUUID) {
        this.hincUUID = hincUUID;
    }

    @Override
    public void process(IoTUnit unit) {
        // save to DB
        unit.setHincID(hincUUID);
        IoTUnitDAO unitDAO = new IoTUnitDAO();
        unitDAO.save(unit);

        // send message to the group topic, message type: UPDATE_INFORMATION
        String groupTopic = HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName());
        HincMessage updateMsg = new HincMessage(HINCMessageType.UPDATE_INFORMATION_SINGLEIOTUNIT.toString(), HincConfiguration.getMyUUID(), groupTopic, "", unit.toJson());
        MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
        FACTORY.getMessagePublisher().pushMessage(updateMsg);
    }

}
