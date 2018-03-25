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
import sinc.hinc.communication.payloads.ControlResourceProviderBody;
import sinc.hinc.communication.payloads.ControlResourceProviderResult;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.local.LocalManagementService;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

/**
 *
 * @author hungld
 */
public class HandleControlResourceProvider implements HINCMessageHander {

    @Override
    public HincMessage handleMessage(HincMessage message) {
        // marshall the parameters and options provided by the user
        ControlResourceProviderBody body = ControlResourceProviderBody.fromJson(message.getPayload());
        System.out.println(message.getPayload());
        ProviderQueryAdaptor adaptor = LocalManagementService.pluginManager.getAdaptorByName(body.getAdaptorName());

        String resourceProviderUuid = body.getResouceProviderUuid();
        AbstractDAO<ResourcesProvider> resourcesProviderDAO = new AbstractDAO<>(ResourcesProvider.class);
        ResourcesProvider provider = resourcesProviderDAO.read(resourceProviderUuid);

        ControlPoint controlPoint = null;
        for(ControlPoint cp: provider.getApis()){
            if(body.getControlPointUuid().equals(cp.getUuid())){
                controlPoint = cp;
                break;
            }
        }

        if(controlPoint != null){
            controlPoint.setParameters(body.getParameters());
        }

        ProviderControlResult controlResult = adaptor.sendControl(controlPoint);

        ControlResourceProviderResult result = new ControlResourceProviderResult();
        result.setResult(ControlResourceProviderResult.CONTROL_RESULT.valueOf(controlResult.getResult().name()));
        result.setOutput(controlResult.getOutput());
        result.setExitcode(controlResult.getExitcode());
        result.setExecutionTime(controlResult.getExecutionTime());
        result.setUpdateIoTUnit(controlResult.getUpdateIoTUnit());

        return new HincMessage(HINCMessageType.CONTROL_RESULT.toString(), HincConfiguration.getMyUUID(), message.getFeedbackTopic(), "", result.toJson());
    }

}
