/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.messageHandlers;

import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.model.API.WrapperMicroService;
import sinc.hinc.model.VirtualComputingResource.MicroService;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

/**
 *
 * @author hungld
 */
public class HandleQueryService implements HINCMessageHander {

    @Override
    public HincMessage handleMessage(HincMessage message) {
        AbstractDAO<MicroService> dao = new AbstractDAO(MicroService.class);
        WrapperMicroService wrapper = new WrapperMicroService(dao.readAll());
        return new HincMessage(HINCMessageType.UPDATE_INFORMATION_MICRO_SERVICE.toString(), HincConfiguration.getMyUUID(), message.getFeedbackTopic(), "", wrapper.toJson());
    }

}
