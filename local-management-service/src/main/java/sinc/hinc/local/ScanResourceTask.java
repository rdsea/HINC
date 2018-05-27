package sinc.hinc.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sinc.hinc.local.plugin.AdaptorManager;

@Component
public class ScanResourceTask {

    @Autowired
    AdaptorManager adaptorManager;

    @Scheduled(fixedRate = 5000)
    public void scanAll() {
        adaptorManager.scanAll();
    }
}
