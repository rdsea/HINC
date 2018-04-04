package sinc.hinc.local;

import org.slf4j.Logger;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.factory.MessageClientFactory;

import java.util.HashSet;

public class LocalManagementService {
    static Logger logger = HincConfiguration.getLogger();
    public static String DEFAULT_SOURCE_SETTINGS = "./sources.conf";
    public static MessageClientFactory FACTORY = MessageClientFactory.getFactory(HincConfiguration.getBroker());

    private HashSet<String> enabledAdaptors = new HashSet<>();
    public static int globalInterval = Integer.parseInt(PropertiesManager.getParameter("global.interval", DEFAULT_SOURCE_SETTINGS));

    public void listen(){

    }

    public static void scanAdaptors(){

    }

}
