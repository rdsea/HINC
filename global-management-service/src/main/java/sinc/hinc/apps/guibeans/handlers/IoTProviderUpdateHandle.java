/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.apps.guibeans.handlers;

import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

/**
 *
 * @author hungld
 */
public class IoTProviderUpdateHandle implements HINCMessageHander {

    @Override
    public HincMessage handleMessage(HincMessage message) {
        System.out.println("IoT Provider is push the HINC global, from: " + message.getSenderID() + ". Content:" + message.getPayload());
        ResourcesProvider unit = ResourcesProvider.fromJson(message.getPayload());
        AbstractDAO<ResourcesProvider> dao = new AbstractDAO<>(ResourcesProvider.class);
        dao.save(unit);
        return null;
    }

}
