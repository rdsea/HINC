package sinc.hinc.local;

import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.factory.MessageClientFactory;
import sinc.hinc.communication.processing.HINCMessageListener;
import sinc.hinc.local.communication.LocalCommunicationManager;
import sinc.hinc.local.plugin.PluginManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class LocalManagementService {
    static Logger logger = HincConfiguration.getLogger();
    public static String DEFAULT_SOURCE_SETTINGS = "./sources.conf";
    //public static final MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
    //public static HINCMessageListener LISTENER = new HINCMessageListener(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
    public static PluginManager pluginManager;
    private LocalCommunicationManager localCommunicationManager;

    private HashSet<String> enabledAdaptors = new HashSet<>();
    public static int globalInterval;

    private String group = "group";
    private String id = "id";
    private String globalExchange = "global_incoming_direct";


    public LocalManagementService(){
        globalInterval = Integer.parseInt(PropertiesManager.getParameter("global.interval", DEFAULT_SOURCE_SETTINGS));
        String enables = PropertiesManager.getParameter("global.enable", DEFAULT_SOURCE_SETTINGS);

        logger.debug("Enabled adaptors are: " + enables + ". Slit it: " + Arrays.toString(enables.split(",")));

        for (String s : enables.split(",")) {
            enabledAdaptors.add(s.trim());
            logger.debug("Enabled adaptor: " + s);
        }

        localCommunicationManager = new LocalCommunicationManager(group, id, globalExchange);


        pluginManager = new PluginManager(enabledAdaptors, localCommunicationManager);
    }

    public static void scanAdaptors(){
        pluginManager.scanAdaptors();
    }

    public void connect() throws IOException, TimeoutException {
        //TODO make configurable
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");


        localCommunicationManager.connect(connectionFactory);
    }

    public void registerAtGlobal(){
        localCommunicationManager.registerAtGlobal();
    }

}
