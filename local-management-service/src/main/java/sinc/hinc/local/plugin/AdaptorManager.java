package sinc.hinc.local.plugin;

import org.slf4j.Logger;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.payloads.Control;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.local.PropertiesManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdaptorManager {

    Map<String, Adaptor> adaptors = new ConcurrentHashMap<>();

    private Logger logger = HincConfiguration.getLogger();
    private static AdaptorManager adaptorManager;

    protected AdaptorManager(){
        //adaptorManager.init();
    }

    public static AdaptorManager getInstance(){
        if(adaptorManager == null){
            adaptorManager = new AdaptorManager();
        }

        return adaptorManager;
    }

    public void addAdaptor(String adaptorName){
        Adaptor adaptor = new Adaptor();
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

    public void sendControl(String adaptorName, Control control, HincMessage.HincMessageDestination reply){
        for(Adaptor adaptor: adaptors.values()){
            if(adaptor.getName().equals(adaptorName)){
                logger.info("sending control to adaptor "+adaptor.getName());
                if(reply.getRoutingKey() == null){
                    reply.setRoutingKey("");
                }
                adaptor.sendControl(control, reply);
                break;
            }
        }
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
