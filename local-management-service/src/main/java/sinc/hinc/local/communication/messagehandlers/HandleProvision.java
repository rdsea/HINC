package sinc.hinc.local.communication.messagehandlers;

import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.Resource;
import sinc.hinc.local.plugin.AdaptorManager;

public class HandleProvision extends HINCMessageHandler {
    public HandleProvision() {
        super(HINCMessageType.PROVISION);
    }

    @Override
    protected void doHandle(HincMessage msg){
        logger.debug("received " + msg.toString());

        try{
            Resource resource = this.objectMapper.readValue(msg.getPayload(), Resource.class);
            logger.debug(resource.getProviderUuid());
            AdaptorManager.getInstance().provisionResource(resource.getProviderUuid(), resource, msg.getReply());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
