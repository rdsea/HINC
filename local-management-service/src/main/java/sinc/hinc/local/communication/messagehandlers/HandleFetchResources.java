package sinc.hinc.local.communication.messagehandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sinc.hinc.common.communication.HINCMessageHandler;
import sinc.hinc.common.communication.HINCMessageType;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

import java.util.ArrayList;
import java.util.List;
@Component
public class HandleFetchResources extends HINCMessageHandler {
    public HandleFetchResources() {
        super(HINCMessageType.FETCH_RESOURCES);
    }

    @Override
    protected HincMessage doHandle(HincMessage hincMessage) {
        AbstractDAO<Resource> resourceAbstractDAO = new AbstractDAO<>(Resource.class);

        List<Resource> resources = new ArrayList<>();
        if(StringUtils.isEmpty(hincMessage.getPayload())){
            resources = resourceAbstractDAO.readAll();
        }else{
            resources = resourceAbstractDAO.query(hincMessage.getPayload());
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

