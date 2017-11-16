/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.local;

import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import sinc.hinc.abstraction.ResourceDriver.ProviderListenerAdaptor;
import sinc.hinc.abstraction.transformer.ExecutionEnvironmentTransformer;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.abstraction.ResourceDriver.ServiceDetector;
import sinc.hinc.abstraction.transformer.PhysicalResourceTransformer;

/**
 *
 * @author hungld
 */
public class PluginRegistry {

    static Logger logger = HincConfiguration.getLogger();

    Set<ProviderQueryAdaptor> adaptors = new HashSet<>();
    Set<ProviderListenerAdaptor> listeners = new HashSet<>();
    Set<PhysicalResourceTransformer> iotUnitTrans = new HashSet<>();
    Set<ExecutionEnvironmentTransformer> executionEnvTrans = new HashSet<>();
    Set<ServiceDetector> serviceDetectors = new HashSet<>();

    Reflections reflections = new Reflections("sinc.hinc");

    public PluginRegistry() {
        // initiate and create plugin instances
        try {

            Set<Class<? extends ProviderQueryAdaptor>> adaptorClasses = reflections.getSubTypesOf(ProviderQueryAdaptor.class);
            Set<Class<? extends ProviderListenerAdaptor>> listenerClasses = reflections.getSubTypesOf(ProviderListenerAdaptor.class);

            Set<Class<? extends PhysicalResourceTransformer>> iotUnitClazz = reflections.getSubTypesOf(PhysicalResourceTransformer.class);

            Set<Class<? extends ExecutionEnvironmentTransformer>> executionEnvClazz = reflections.getSubTypesOf(ExecutionEnvironmentTransformer.class);

            Set<Class<? extends ServiceDetector>> serviceDetectorClazz = reflections.getSubTypesOf(ServiceDetector.class);

            for (Class<? extends ProviderListenerAdaptor> clazz : listenerClasses) {
                listeners.add(clazz.newInstance());
            }

            for (Class<? extends ProviderQueryAdaptor> clazz : adaptorClasses) {
                adaptors.add(clazz.newInstance());
            }

            for (Class<? extends PhysicalResourceTransformer> clazz : iotUnitClazz) {
                iotUnitTrans.add(clazz.newInstance());
            }

            for (Class<? extends ExecutionEnvironmentTransformer> clazz : executionEnvClazz) {
                executionEnvTrans.add(clazz.newInstance());
            }

            logger.debug("========> PluginRegistry: register service detector: " + serviceDetectorClazz.size());
            for (Class<? extends ServiceDetector> clazz : serviceDetectorClazz) {
                serviceDetectors.add(clazz.newInstance());
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error("Cannot instantiate the instance of a plugin");
            ex.printStackTrace();
        }

    }

    public PhysicalResourceTransformer getIoTUnitTranformerByName(String name) {
        for (PhysicalResourceTransformer a : iotUnitTrans) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }

    public ExecutionEnvironmentTransformer getExecutionEnvTransformerByName(String name) {
        for (ExecutionEnvironmentTransformer a : executionEnvTrans) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }

    public Set<ServiceDetector> getServiceDetectors() {
        return serviceDetectors;
    }

    public Set<ProviderQueryAdaptor> getAdaptors() {
        return adaptors;
    }

    public Set<ProviderListenerAdaptor> getListeners() {
        return listeners;
    }

}
