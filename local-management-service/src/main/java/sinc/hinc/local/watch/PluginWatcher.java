package sinc.hinc.local.watch;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.local.plugin.PluginManager;

public class PluginWatcher {
    static Logger logger = HincConfiguration.getLogger();
    public static long POLL_INTERVAL = 5000;
    public static String PLUGIN_FOLDER = "plugins";

    private FileAlterationObserver observer = new FileAlterationObserver(PLUGIN_FOLDER);
    private FileAlterationMonitor monitor = new FileAlterationMonitor(POLL_INTERVAL);




    public PluginWatcher(PluginManager manager){
        observer.addListener(new FileListener(manager));
        monitor.addObserver(observer);
    }

    public void startMonitoring(){
        try {
            monitor.start();
            logger.info("Started to monitor plugins, polling every "+POLL_INTERVAL+" seconds");
        } catch (Exception e) {
            logger.error("failed to start file watch service");
            e.printStackTrace();
        }
    }
}
