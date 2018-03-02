package sinc.hinc.local.watch;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.local.plugin.PluginManager;

import java.io.File;
import java.net.URL;

public class FileListener implements FileAlterationListener {
    static Logger logger = HincConfiguration.getLogger();

    private PluginManager manager;

    public FileListener(PluginManager manager){
        this.manager = manager;
    }

    public void onStart(FileAlterationObserver fileAlterationObserver) {
    }

    public void onDirectoryCreate(File file) {
        logger.info("new directory created, wtf are you doing???!!???");
    }

    public void onDirectoryChange(File file) {
        logger.info("directory changed");
    }

    public void onDirectoryDelete(File file) {
        logger.info("director created, wtf are you doing?");
    }

    public void onFileCreate(File file) {
        try {
            URL url = file.toURI().toURL();
            this.manager.loadPlugin(url);

        } catch (Exception e) {
            logger.error("failed to load plugin");
            e.printStackTrace();
        }
    }

    public void onFileChange(File file) {
        logger.info("file changed");
    }

    public void onFileDelete(File file) {
        logger.info("file deleted");
    }

    public void onStop(FileAlterationObserver fileAlterationObserver) {
    }
}

