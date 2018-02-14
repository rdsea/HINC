package sinc.hinc.logstash.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.abstraction.ResourceDriver.ServiceDetector;
import sinc.hinc.model.SoftwareArtifact.MicroserviceArtifact;

import java.util.Map;

public class LogstashDetector implements ServiceDetector {

    private static Logger logger = LoggerFactory.getLogger(LogstashDetector.class.getSimpleName());

    @Override
    public MicroserviceArtifact detect(Map<String, String> settings) {
        MicroserviceArtifact microserviceArtifact = new MicroserviceArtifact();


        String sourceEndpoint;
        String endpoint;
        String resourceID;
        String name;
        String hostID;
        Map<String, String> meta;

        microserviceArtifact.setName("logstashMicroservice");


        logger.info("detect.settings:");
        for(String key: settings.keySet()){
            logger.info(key + ": " + settings.get(key));
        }

        return microserviceArtifact;
    }

    @Override
    public String getName() {
        return "logstashPlugin";
    }
}
