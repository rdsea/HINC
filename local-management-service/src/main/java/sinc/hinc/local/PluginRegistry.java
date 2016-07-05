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
import sinc.hinc.abstraction.ResourceDriver.ProviderAdaptor;
import sinc.hinc.abstraction.transformer.ConnectivityTransformater;
import sinc.hinc.abstraction.transformer.ControlPointTransformer;
import sinc.hinc.abstraction.transformer.DataPointTransformer;
import sinc.hinc.abstraction.transformer.ExecutionEnvironmentTransformer;
import sinc.hinc.common.utils.HincConfiguration;

/**
 *
 * @author hungld
 */
public class PluginRegistry {

    static Logger logger = HincConfiguration.getLogger();

    Set<Class<? extends ProviderAdaptor>> adaptorClasses = new HashSet<>();
    Set<Class<? extends DataPointTransformer>> datapoints = new HashSet<>();
    Set<Class<? extends ControlPointTransformer>> controlpoints = new HashSet<>();
    Set<Class<? extends ExecutionEnvironmentTransformer>> environments = new HashSet<>();
    Set<Class<? extends ConnectivityTransformater>> connectivities = new HashSet<>();
    Set<ProviderAdaptor> adaptors;

    public PluginRegistry() {
        Reflections reflections = new Reflections("sinc.hinc");
        adaptorClasses.addAll((reflections.getSubTypesOf(ProviderAdaptor.class)));
        datapoints.addAll((reflections.getSubTypesOf(DataPointTransformer.class)));
        controlpoints.addAll((reflections.getSubTypesOf(ControlPointTransformer.class)));
        environments.addAll((reflections.getSubTypesOf(ExecutionEnvironmentTransformer.class)));
        connectivities.addAll((reflections.getSubTypesOf(ConnectivityTransformater.class)));

        adaptors = initiateAdaptors();
    }

    private Set<ProviderAdaptor> initiateAdaptors() {
        Set<ProviderAdaptor> a = new HashSet();
        for (Class<? extends ProviderAdaptor> clazz : adaptorClasses) {
            try {
                ProviderAdaptor p = clazz.newInstance();
                if (p != null) {
                    a.add(p);
                    logger.debug("Register adaptor: {}, class: {}", p.getName(), clazz);                    
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return a;
    }

    public DataPointTransformer getDatapointTransformerByName(String name) {
        for (Class<? extends DataPointTransformer> a : datapoints) {
            if (a.getName().toLowerCase().trim().equals(name.trim().toLowerCase())) {
                try {
                    return a.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    logger.error("Cannot instantiate DataPointTransformer: " + name);
                    return null;
                }
            }
        }
        return null;
    }

    public ControlPointTransformer getControlpointTransformerByName(String name) {
        for (Class<? extends ControlPointTransformer> a : controlpoints) {
            if (a.getName().toLowerCase().trim().equals(name.trim().toLowerCase())) {
                try {
                    return a.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    logger.error("Cannot instantiate ControlPointTransformer: " + name);
                    return null;
                }
            }
        }
        return null;
    }

    public ExecutionEnvironmentTransformer getExecutionEnvTransformerByName(String name) {
        for (Class<? extends ExecutionEnvironmentTransformer> a : environments) {
            if (a.getName().toLowerCase().trim().equals(name.trim().toLowerCase())) {
                try {
                    return a.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    logger.error("Cannot instantiate ExecutionEnvironmentTransformer: " + name);
                    return null;
                }
            }
        }
        return null;
    }

    public ConnectivityTransformater getConnectivityTransformerByName(String name) {
        for (Class<? extends ConnectivityTransformater> a : connectivities) {
            if (a.getName().toLowerCase().trim().equals(name.trim().toLowerCase())) {
                try {
                    return a.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    logger.error("Cannot instantiate ConnectivityTransformater: " + name);
                    return null;
                }
            }
        }
        return null;
    }

    public Set<ProviderAdaptor> getAdaptors() {
        return adaptors;
    }

}
