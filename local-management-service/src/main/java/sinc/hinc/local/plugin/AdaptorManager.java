package sinc.hinc.local.plugin;

import org.slf4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.model.payloads.Control;
import sinc.hinc.common.utils.HincConfiguration;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope(value = "singleton")
public class AdaptorManager {

    @Value("${adaptor.amqp.input}")
    private String adaptorInputQueue;
    @Value("${adaptor.amqp.output.unicast}")
    private String adaptorOutputUnicastExchange;
    @Value("${hinc.local.group}")
    private String group;
    @Value("${hinc.local.id}")
    private String id;

    Map<String, Adaptor> adaptors = new ConcurrentHashMap<>();
    @Autowired
    RabbitTemplate rabbitTemplate;

    private Logger logger = HincConfiguration.getLogger();

    public void addAdaptor(String adaptorName){
        Adaptor adaptor = new Adaptor(rabbitTemplate, adaptorOutputUnicastExchange, adaptorInputQueue, group, id);
        adaptor.setName(adaptorName);
        logger.info("registered new adaptor "+adaptorName);
        adaptors.put(adaptorName, adaptor);
    }

    public void removeAdaptor(String adaptorName){
        logger.info("removing adaptor "+adaptorName);
        adaptors.remove(adaptorName);
    }

    public void scanAll(){
        logger.info("scanning adaptors");
        for(Adaptor adaptor: adaptors.values()){
            adaptor.scanResources();
            adaptor.scanResourceProvider();
        }
    }

    public void sendControl(String adaptorName, Control control){

    }

    public Resource provisionResource(String adaptorName, Resource resource) throws IOException {
        for(Adaptor adaptor: adaptors.values()){
            logger.debug(adaptor.getName());
            if(adaptor.getName().equals(adaptorName)){
                logger.info("sending control to adaptor "+adaptor.getName());
                return adaptor.provisionResource(resource);
            }
        }
        return null;
    }

    public Resource deleteResource(String adaptorName, Resource resource) throws IOException {
        for(Adaptor adaptor: adaptors.values()){
            logger.debug(adaptor.getName());
            if(adaptor.getName().equals(adaptorName)){
                logger.info("sending control to adaptor "+adaptor.getName());
                return adaptor.deleteResource(resource);
            }
        }
        return null;
    }

    public Map<String, Adaptor> getAdaptors() {
        return adaptors;
    }

    public void setAdaptors(Map<String, Adaptor> adaptors) {
        this.adaptors = adaptors;
    }

    public void addAdaptor(String name, Adaptor adaptor){
        this.adaptors.put(name, adaptor);
    }

}
