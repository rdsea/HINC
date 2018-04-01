package sinc.hinc.local.messageHandlers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.processing.HINCMessageHander;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.model.Resource;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class HandleQueryResourcesReply implements HINCMessageHander {
    Logger logger = HincConfiguration.getLogger();

    @Override
    public HincMessage handleMessage(HincMessage hincMessage) {
        logger.debug("Received new resources: " + hincMessage.toJson());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Resource[] resourceArray = new Resource[0];
        try {
            resourceArray = mapper.readValue(hincMessage.getPayload(), Resource[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Resource> resources = (ArrayList<Resource>) Arrays.asList(resourceArray);
        logger.debug(resources.size()+" resources found");
        AbstractDAO<Resource> resourceDAO = new AbstractDAO<>(Resource.class);

        resourceDAO.saveAll(resources);

        // no reply needed
        return null;
    }
}
