/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.teit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import sinc.hinc.abstraction.transformer.IoTUnitTransformer;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capabilities.DataPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;

/**
 *
 * @author hungld
 */
public class TeitSensorTransformer implements IoTUnitTransformer<Properties> {

    @Override
    public IoTUnit translateIoTUnit(Properties prop) {
        // basic of the 
        IoTUnit unit = new IoTUnit(prop.getProperty("sensorID"));
        unit.hasPhysicalType("Sensor");
        DataPoint datapoint = new DataPoint(prop.getProperty("sensorID"), prop.getProperty("sensorType"));
        String measurement = prop.getProperty("measurement");
        if (measurement != null && !measurement.isEmpty()) {
            datapoint.setMeasurementUnit(measurement);
        } else {
            datapoint.setMeasurementUnit("unknown");
        }
        String apiName = mapApiName(prop.getProperty("platform"));
        datapoint.setDataApi(apiName);

        Map<String, String> dataApiSettings = new HashMap<>();
        for (Entry<Object, Object> entry : prop.entrySet()) {
            String key = entry.getKey().toString().trim();
            if (key.startsWith("platform." + apiName)) {
                String shortKey = key.substring(key.lastIndexOf(".") + 1).trim();
                dataApiSettings.put(shortKey, entry.getValue().toString().trim());

                // now update the AccessPoint (only MQTT at the moment, but more should be done)
                if (apiName.equals("mqtt") && shortKey.equals("url")) {
                    datapoint.setConnectingTo(new AccessPoint(entry.getValue().toString().trim()));
                }
            }
        }
        datapoint.setDataApiSettings(dataApiSettings);
        unit.hasDatapoint(datapoint);
        unit.setControlpoints(getListOfControlPoint(prop));
        return unit;
    }

    @Override
    public String getName() {
        return "teit-sensor";
    }

    private String mapApiName(String className) {
        switch (className) {
            case "teit.sensor.PlatformConsole.ConsolePlatform":
                return "console";
            case "teit.sensor.PlatformSparkFun.SparkfunPlatform":
                return "sparkfun";
            case "teit.sensor.PlatformThingSpeak.ThingSpeakPlatform":
                return "thingspeak";
            case "teit.sensor.MQTT.MQTTOutput":
                return "mqtt";
            default:
                return "unknown";
        }
    }

    private Set<ControlPoint> getListOfControlPoint(Properties data) {
        String sensorScript = data.getProperty("executionscript");

        Set<ControlPoint> cps = new HashSet<>();
        try {
            Process p = Runtime.getRuntime().exec("bash " + sensorScript + " list-commands");

            p.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            System.out.println("Execute command: " + "bash " + sensorScript + " list-commands");
            while ((line = reader.readLine()) != null) {
                System.out.println("Adding controlpoint from: " + line);
                line = line.trim() + " ";
                ControlPoint cp = new ControlPoint(line.substring(0, line.indexOf(" ")), ControlPoint.InvokeProtocol.LOCAL_EXECUTE, sensorScript + " " + line.trim());
                System.out.println("  --> Controlpoint added: " + cp.getReference());

                if (cp.getName().equals("connect-mqtt")) {
                    cp.setControlType(ControlPoint.ControlType.CONNECT_TO_NETWORK);
                    cp.hasCondition("network-type", NetworkFunctionService.NetworkServiceType.BROKER_MQTT.toString());
                } else {
                    cp.setControlType(ControlPoint.ControlType.SELF_CONFIGURE);
                }
                cps.add(cp);
            }
            return cps;
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            return cps;
        }
    }

}
