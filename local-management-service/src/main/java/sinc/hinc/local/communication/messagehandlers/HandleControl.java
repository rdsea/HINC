
package sinc.hinc.local.communication.messagehandlers;

import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.ResourceProvider;
import sinc.hinc.common.model.capabilities.ControlPoint;
import sinc.hinc.common.model.payloads.Control;
import sinc.hinc.local.communication.AdaptorCommunicationManager;
import sinc.hinc.local.communication.LocalCommunicationManager;
import sinc.hinc.local.plugin.AdaptorManager;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

import java.io.IOException;

public class HandleControl extends HINCMessageHandler {
    public HandleControl() {
        super(HINCMessageType.CONTROL);
    }

    @Override
    protected void doHandle(HincMessage msg) {
        logger.debug("received " + msg.toString());
        AbstractDAO<ResourceProvider> resourceProviderDAO = new AbstractDAO<>(ResourceProvider.class);
        try {
            Control control = this.objectMapper.readValue(msg.getPayload(), Control.class);

            // fetch access point data from selected control point
            ResourceProvider provider = resourceProviderDAO.read(control.getResourceProviderUuid());
            for(ControlPoint controlPoint: provider.getManagementPoints()){
                if(controlPoint.getUuid().equals(control.getControlPointUuid()))
                    control.setAccessPoints(controlPoint.getAccessPoints());
            }

            AdaptorManager.getInstance().sendControl(control.getResourceProviderUuid(), control, msg.getReply());
        } catch (IOException e) {
            logger.error("failed to marshall control payload "+msg.getPayload());
            e.printStackTrace();
        }

    }
}

