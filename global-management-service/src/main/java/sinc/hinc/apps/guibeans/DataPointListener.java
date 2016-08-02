/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.apps.guibeans;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import sinc.hinc.common.utils.HincConfiguration;

/**
 *
 * @author hungld
 */
public class DataPointListener implements Runnable {

    public static class DataSeries {

        Queue<Float> values;
        Float max = Float.MIN_VALUE;
        Float min = Float.MAX_VALUE;
        public static final int QUEUE_SIZE = 100;
        String lastTimeStamp = "";

        public DataSeries() {
            values = new ArrayBlockingQueue<>(QUEUE_SIZE + 2, false);

        }

        public Float getMax() {
            return max;
        }

        public void setMax(Float max) {
            this.max = max;
        }

        public Float getMin() {
            return min;
        }

        public void setMin(Float min) {
            this.min = min;
        }

        public Queue<Float> getValues() {
            return values;
        }

        public void offer(Float value, String timeStamp) {
            if (timeStamp.equals(lastTimeStamp)) {
                return;
            }
//            System.out.println("Queue size: " + values.size() + "/" + QUEUE_SIZE);
            if (values.size() > QUEUE_SIZE) {
//                System.out.println("  -->Poll: " + values.size() + "/" + QUEUE_SIZE);
                values.poll();
            }
            if (value > max) {
                max = value;
            }
            if (value < min) {
                min = value;
            }
//            values.offer(new Float(formatter.format(value)));
            lastTimeStamp = timeStamp;
            values.offer(value);
        }

        public Float poll() {
            return values.poll();
        }

        public Float peek() {
            return values.peek();
        }

        public boolean isChanged(String timeStamp) {
            return !timeStamp.equals(lastTimeStamp);
        }

    }
    public static DataPointListener INSTANCE = new DataPointListener();

    static Thread listenerThread;
    // map of sensor ID and lastest QUEUE_SIZE data point
    static Map<String, DataSeries> subscribingData = new HashMap<>();

    static String broker = HincConfiguration.getDataForward();
    static String topic = "mysensor1234"; // hard code here
    static TypeReference typeRerefence = new TypeReference<Map<String, String>>() {
    };

    public static void makeSureListening() {
        if (listenerThread == null) {
            listenerThread = new Thread(new DataPointListener());
            listenerThread.start();
        }
    }

    public static DataSeries getUpdateDataSeries(String resourceid) {
        DataSeries dataSeries = subscribingData.get(resourceid);
        if (dataSeries == null) {
            dataSeries = new DataSeries();
            subscribingData.put(resourceid, dataSeries);
        }
        return dataSeries;
    }

    public static boolean isListeningOrAnalyzed(String resourceIdOrAnalyticName) {
        return subscribingData.containsKey(resourceIdOrAnalyticName);
    }

    @Override
    public void run() {

        MqttCallback callBack = new MqttCallback() {
            @Override
            public void connectionLost(Throwable thrwbl) {
                System.out.println("MQTT is disconnected from topic: " + topic + ". Message: " + thrwbl.getMessage() + ". Cause: " + thrwbl.getCause().getMessage());
                thrwbl.printStackTrace();
            }

            @Override
            public void messageArrived(String string, MqttMessage mm) throws Exception {
                String msg = new String(mm.getPayload(), StandardCharsets.UTF_8);
                System.out.println(msg);

                // clear old forwardingDatapoints
                String payloadMap = new String(mm.getPayload());
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> sensorDataItem = mapper.readValue(payloadMap, typeRerefence);
                String sensorID = sensorDataItem.get("sensorid");
                float value = Float.parseFloat(sensorDataItem.get("value"));
                String timeStamp = sensorDataItem.get("timestamp");

                DataSeries dataseries = getUpdateDataSeries(sensorID);

                dataseries.offer(value, timeStamp);
            }

            @Override

            public void deliveryComplete(IMqttDeliveryToken imdt) {
                System.out.println("Deliver complete to topic: " + topic);
            }
        };

// connect and subscribe
        MqttClient queueClient;
        String clientId = UUID.randomUUID().toString();
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            queueClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            queueClient.setCallback(callBack);
            queueClient.connect(connOpts);
            if (queueClient.isConnected()) {
                System.out.println("Connected to the MQTT broker: " + broker);
            } else {
                System.out.println("Failed to connect to the broker: " + broker);
            }
            queueClient.subscribe(topic);
            System.out.println("Subscribed the topic: " + topic + ".\nListening to the incoming data... \n");
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

}
