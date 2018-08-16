package sinc.hinc.local.communication.messagehandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.local.repository.ResourceRepository;

import java.util.ArrayList;
import java.util.List;
@Component
public class HandleFetchResources extends HINCMessageHandler {
    public HandleFetchResources() {
        super(HINCMessageType.FETCH_RESOURCES);
    }

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    protected HincMessage doHandle(HincMessage hincMessage) {

        List<Resource> resources = new ArrayList<>();
        if(StringUtils.isEmpty(hincMessage.getPayload())){
            resources = resourceRepository.readAll();
        }else{
            resources = resourceRepository.query(hincMessage.getPayload());
        }

        logger.info(resources.size()+ " resources fetched");
        try {
            String payload = this.objectMapper.writeValueAsString(resources);
            HincMessage message = new HincMessage(
                    HINCMessageType.DELIVER_RESOURCES,
                    HincConfiguration.getMyUUID(),
                    payload
            );

            return message;
        } catch (JsonProcessingException e) {
            logger.error("failed to serialize payload for "+this.messageType);
            e.printStackTrace();
        }
        return null;
    }


}

