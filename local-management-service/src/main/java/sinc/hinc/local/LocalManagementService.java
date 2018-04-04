package sinc.hinc.local;

import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.local.communication.LocalCommunicationManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeoutException;

public class LocalManagementService {
    static Logger logger = HincConfiguration.getLogger();
    public static String DEFAULT_SOURCE_SETTINGS = "./sources.conf";
    private LocalCommunicationManager localCommunicationManager;

    private HashSet<String> enabledAdaptors = new HashSet<>();
    public static int globalInterval  = Integer.parseInt(PropertiesManager.getParameter("global.interval", DEFAULT_SOURCE_SETTINGS));;

    private String host = "localhost";
    private String group = "group";
    private String id = "id";
    private String globalExchange = "global_incoming_direct";


    public LocalManagementService() throws IOException, TimeoutException {
        globalInterval = Integer.parseInt(PropertiesManager.getParameter("global.interval", DEFAULT_SOURCE_SETTINGS));
        String enables = PropertiesManager.getParameter("global.enable", DEFAULT_SOURCE_SETTINGS);

        logger.debug("Enabled adaptors are: " + enables + ". Slit it: " + Arrays.toString(enables.split(",")));

        for (String s : enables.split(",")) {
            enabledAdaptors.add(s.trim());
            logger.debug("Enabled adaptor: " + s);
        }



        //TODO read config
        LocalCommunicationManager.initialize(host, group, id, globalExchange);
        localCommunicationManager = LocalCommunicationManager.getInstance();

    }

    public void listen(){

    }

    public static void scanAdaptors(){

    }



}
