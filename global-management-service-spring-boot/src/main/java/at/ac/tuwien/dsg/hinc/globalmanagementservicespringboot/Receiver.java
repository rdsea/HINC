package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot;

import java.util.concurrent.CountDownLatch;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(byte[] message) {
        String msg = new String(message);
        System.out.println("Received HINC <" + msg + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}
