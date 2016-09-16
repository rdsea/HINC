/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.teit.actuator;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import sinc.hinc.abstraction.ResourceDriver.IoTUnitProcessor;
import sinc.hinc.abstraction.ResourceDriver.ProviderListenerAdaptor;
import sinc.hinc.abstraction.ResourceDriver.utils.FilesScanner;
import sinc.hinc.abstraction.transformer.IoTUnitTransformer;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.transformer.teit.FileWatcherWithTimer;
import teit.enumactuator.model.EnumActuator;

/**
 *
 * @author hungld
 */
public class TeitActuatorAdaptorPush implements ProviderListenerAdaptor {

    @Override
    public void listen(Map<String, String> settings, final IoTUnitTransformer tranformer, final IoTUnitProcessor processor) {
        String workingDir = settings.get("workingdir");
        System.out.println("Settings of TEIT actuator adaptor: " + settings);
        System.out.println("Working dir is: " + workingDir);
        Map<String, String> fileScannerSettings = new HashMap<>();
        fileScannerSettings.put("path", workingDir);
        fileScannerSettings.put("filter", "actuator.json");
        Map<String, String> metas = FilesScanner.getItems(fileScannerSettings);

        for (final Map.Entry<String, String> entry : metas.entrySet()) {
            System.out.println("Monitoring file: " + entry.getKey());
            // start a timmer to check
            TimerTask task = new FileWatcherWithTimer(new File(entry.getKey())) {
                @Override
                protected void onChange(File file) {
                    System.out.println("The actuator file is changed: " + file.getAbsolutePath());
                    String actuatorJar = entry.getKey().replace("actuator.json", "actuator.sh");
                    EnumActuator actuator = EnumActuator.fromJsonFile(new File(entry.getKey()));
                    System.out.println("Actuator: " + actuator.getName() + " change, adapting..");
                    actuator.setActuatorJar(actuatorJar);
                    IoTUnit unit = tranformer.translateIoTUnit(actuator);
                    processor.process(unit);
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, new Date(), 1000); // check file every second
        }
    }

    @Override
    public String getName() {
        return "teit-actuator";
    }

}
