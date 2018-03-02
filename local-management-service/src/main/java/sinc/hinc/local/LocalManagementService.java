package sinc.hinc.local;

import org.slf4j.Logger;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.factory.MessageClientFactory;
import sinc.hinc.communication.processing.HINCMessageListener;
import sinc.hinc.local.plugin.PluginManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class LocalManagementService {
    static Logger logger = HincConfiguration.getLogger();
    public static String DEFAULT_SOURCE_SETTINGS = "./sources.conf";
    public static final MessageClientFactory FACTORY = new MessageClientFactory(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
    public static HINCMessageListener LISTENER = new HINCMessageListener(HincConfiguration.getBroker(), HincConfiguration.getBrokerType());
    public static PluginManager pluginManager;

    private HashSet<String> enabledAdaptors = new HashSet<>();
    public static int globalInterval;

    public LocalManagementService(){
        globalInterval = Integer.parseInt(PropertiesManager.getParameter("global.interval", DEFAULT_SOURCE_SETTINGS));
        String enables = PropertiesManager.getParameter("global.enable", DEFAULT_SOURCE_SETTINGS);

        logger.debug("Enabled adaptors are: " + enables + ". Slit it: " + Arrays.toString(enables.split(",")));

        for (String s : enables.split(",")) {
            enabledAdaptors.add(s.trim());
            logger.debug("Enabled adaptor: " + s);
        }

        pluginManager = new PluginManager(enabledAdaptors);
    }

    public static void scanAdaptors(){
        pluginManager.scanAdaptors();
    }

}
