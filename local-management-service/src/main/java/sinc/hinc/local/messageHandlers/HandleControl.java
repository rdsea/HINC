/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local.messageHandlers;

import sinc.hinc.abstraction.ResourceDriver.ProviderControlResult;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.payloads.ControlBody;
import sinc.hinc.communication.payloads.ControlResult;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.local.LocalManagementService;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

import java.util.Map;

/**
 *
 * @author hungld
 */
public class HandleControl implements HINCMessageHander {

    @Override
    public HincMessage handleMessage(HincMessage message) {
        // marshall the parameters and options provided by the user
        ControlBody body = ControlBody.fromJson(message.getPayload());
        ProviderQueryAdaptor adaptor = LocalManagementService.pluginManager.getAdaptorByName(body.getAdaptorName());

        String controlPointUUID = body.getControlPointUUID();
        AbstractDAO<ControlPoint> controlPointDAO = new AbstractDAO<>(ControlPoint.class);
        ControlPoint controlPoint = controlPointDAO.read(controlPointUUID);
        ProviderControlResult controlResult = adaptor.sendControl(controlPoint);

        ControlResult result = new ControlResult();
        result.setResult(ControlResult.CONTROL_RESULT.valueOf(controlResult.getResult().name()));
        result.setOutput(controlResult.getOutput());
        result.setExitcode(controlResult.getExitcode());
        result.setExecutionTime(controlResult.getExecutionTime());
        result.setUpdateIoTUnit(controlResult.getUpdateIoTUnit());

        return new HincMessage(HINCMessageType.CONTROL_RESULT.toString(), HincConfiguration.getMyUUID(), message.getFeedbackTopic(), "", result.toJson());
    }

}
