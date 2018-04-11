package sinc.hinc.local.plugin;

import org.slf4j.Logger;
import sinc.hinc.common.communication.HincMessage;
import sinc.hinc.common.model.payloads.Control;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.local.PropertiesManager;

import java.util.*;

public class AdaptorManager {

    Map<String, Adaptor> adaptors = new HashMap<>();

    public static String DEFAULT_SOURCE_SETTINGS = "./sources.conf";
    private Logger logger = HincConfiguration.getLogger();
    private static AdaptorManager adaptorManager;

    protected AdaptorManager(){
        //adaptorManager.init();
    }

    public static AdaptorManager getInstance(){
        if(adaptorManager == null){
            adaptorManager = new AdaptorManager();
            adaptorManager.init();
        }

        return adaptorManager;
    }

    // detect enabled adaptors from source config
    public void init(){
        String enables = PropertiesManager.getParameter("global.enable", DEFAULT_SOURCE_SETTINGS);
        String exchange = PropertiesManager.getParameter("global.exchange", DEFAULT_SOURCE_SETTINGS);
        logger.debug("Enabled adaptors are: " + enables + ". Slit it: " + Arrays.toString(enables.split(",")));
        for (String s : enables.split(",")) {
            String adaptorName = s.trim();
            Adaptor adaptor = new Adaptor();
            adaptor.setName(adaptorName);
            adaptor.setExchange(exchange);
            adaptor.setSettings(PropertiesManager.getSettings(adaptorName, DEFAULT_SOURCE_SETTINGS));

            this.addAdaptor(adaptorName, adaptor);
            logger.debug("Enabled adaptor: " + s);
        }
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
