/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.apps.guibeans;

import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.repository.DAO.orientDB.IoTUnitDAO;

/**
 *
 * @author hungld
 */
public class SingleIoTUnitUpdateHandler implements HINCMessageHander {

    @Override
    public HincMessage handleMessage(HincMessage message) {
        System.out.println("A single IoT Unit is push the HINC global, from: " + message.getSenderID() + ". Content:" + message.getPayload());
        IoTUnit unit = IoTUnit.fromJson(message.getPayload());
        IoTUnitDAO dao = new IoTUnitDAO();
        dao.save(unit);
        return null;
    }

}
