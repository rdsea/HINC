/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.teit.sensor;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import sinc.hinc.abstraction.ResourceDriver.IoTUnitProcessor;
import sinc.hinc.abstraction.ResourceDriver.ProviderListenerAdaptor;
import sinc.hinc.abstraction.ResourceDriver.utils.FilesScanner;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.transformer.teit.FileWatcherWithTimer;
import sinc.hinc.abstraction.transformer.PhysicalResourceTransformer;

/**
 *
 * @author hungld
 */
public class TeitSensorAdaptorPush implements ProviderListenerAdaptor {

    @Override
    public void listen(Map<String, String> settings, final PhysicalResourceTransformer tranformer, final IoTUnitProcessor processor) {
        String workingDir = settings.get("workingdir");
        System.out.println("Settings of TEIT actuator adaptor: " + settings);
        System.out.println("Working dir is: " + workingDir);
        Map<String, String> fileScannerSettings = new HashMap<>();
        fileScannerSettings.put("path", workingDir);
        fileScannerSettings.put("filter", "sensor.conf");
        Map<String, String> metas = FilesScanner.getItems(fileScannerSettings);

        for (final Map.Entry<String, String> entry : metas.entrySet()) {
            System.out.println("Monitoring file: " + entry.getKey());
            // start a timmer to check
            TimerTask task = new FileWatcherWithTimer(new File(entry.getKey())) {
                @Override
                protected void onChange(File file) {
                    try {
                        System.out.println("The sensor.conf file is changed: " + file.getAbsolutePath());
                        Properties prop = new Properties();
                        StringReader reader = new StringReader(entry.getValue());
                        prop.load(reader);
                        prop.put("executionscript", entry.getKey().replace("sensor.conf", "sensor.sh"));
                        // These two lines are mandatory to invoke HINC internal processing
                        IoTUnit unit = tranformer.translateIoTUnit(prop);
                        processor.process(unit);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, new Date(), 1000); // check file every second
        }
    }

    @Override
    public String getName() {
        return "teit-sensor";
    }

}
