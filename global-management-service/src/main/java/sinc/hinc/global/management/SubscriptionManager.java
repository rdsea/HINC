/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.management;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.common.metadata.HincMessage;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.communication.messageInterface.MessageClientFactory;
import sinc.hinc.communication.messageInterface.MessagePublishInterface;
import sinc.hinc.communication.messageInterface.MessageSubscribeInterface;
import sinc.hinc.communication.messageInterface.SalsaMessageHandling;
import sinc.hinc.communication.messagePayloads.UpdateGatewayStatus;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author hungld
 */
public class SubscriptionManager {

    static Logger logger = LoggerFactory.getLogger("DELISE");
    MessageClientFactory FACTORY;
    /**
     * TODO: use the default topic to distinguish different client and managmeent scope E.g. each stakeholder will hold an ID, which regarding to the topic
     */
    String prefixTopic = "";
    String groupName;

    public SubscriptionManager(String groupName, String broker, String brokerType) {
        this.groupName = groupName;
        this.FACTORY = new MessageClientFactory(broker, brokerType);
    }

    public void querySoftwareDefinedGateway_Broadcast(long timeout) {
        System.out.println("Start broadcasting the subscription...");
        File dir = new File("log/queries/data");
        dir.mkdirs();
        System.out.println("Data is stored in: " + dir.getAbsolutePath());
        final long startTimeStamp = (new Date()).getTime();

        String eventFileName = "log/queries/" + startTimeStamp + ".event";
        String dataFileName = "log/queries/data/" + startTimeStamp + ".data";
        //final List<SoftwareDefinedGateway> gateways = new ArrayList<>();

        MessagePublishInterface pub = FACTORY.getMessagePublisher();
        // note that when using callFunction, no need to declare the feedbackTopic. This will be filled by the call
        String feedBackTopic = HincMessageTopic.getTemporaryTopic();

        MessageSubscribeInterface sub = FACTORY.getMessageSubscriber(new SalsaMessageHandling() {
            final long windowSize = 30;
            double currentThroughput = 0L;

            @Override
            public void handleMessage(HincMessage message) {
                // this technique to measure the throughput
                Cache<String, String> autoExpireCache = CacheBuilder.newBuilder()
                        .concurrencyLevel(4)
                        .weakKeys()
                        .maximumSize(10000)
                        .expireAfterWrite(windowSize, TimeUnit.SECONDS)
                        .build(
                                new CacheLoader<String, String>() {
                                    public String load(String key) throws Exception {
                                        return key;
                                    }
                                });
                String payload = message.getPayload();
                autoExpireCache.put(payload, payload);

                System.out.println("Get a response message from " + message.getFromSalsa());
                UpdateGatewayStatus updateStatus = UpdateGatewayStatus.fromJson(message.getPayload());
                System.out.println("update status: appear " + updateStatus.getAppear().size() + ", disappear: " + updateStatus.getDisappear().size());

                // record time 
                Long currentTS = (new Date()).getTime();
                Long transferTime = currentTS - updateStatus.getTimeStamp();
                //int currentDataSize = message.getPayload().length();
                long currentSize = autoExpireCache.size() * payload.length();
                currentThroughput = (currentThroughput + ((double) currentSize / 30) * 0.3) / 1.3;

                // here just record time and throughtput, no actual update yet
            }
        });
        sub.subscribe(feedBackTopic, timeout);

        HincMessage queryMessage = new HincMessage(HincMessage.MESSAGE_TYPE.RPC_QUERY_SDGATEWAY_LOCAL, groupName, HincMessageTopic.getCollectorTopicBroadcast(groupName), feedBackTopic, "");
        pub.pushMessage(queryMessage);

        // wait for a few second
        try {
            System.out.println("Wait for " + timeout + " miliseconds ...........");
            Thread.sleep(timeout);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }


}
