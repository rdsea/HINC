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

    Set<ProviderAdaptor> adaptors = new HashSet<>();
    Set<DataPointTransformer> datapointTrans = new HashSet<>();
    Set<ControlPointTransformer> controlPointTrans = new HashSet<>();
    Set<ExecutionEnvironmentTransformer> executionEnvTrans = new HashSet<>();
    Set<ConnectivityTransformater> connectivityTrans = new HashSet<>();

    Reflections reflections = new Reflections("sinc.hinc");

    public PluginRegistry() {
        // initiate and create plugin instances
        try {
            Set<Class<? extends ProviderAdaptor>> adaptorClasses = reflections.getSubTypesOf(ProviderAdaptor.class);
            Set<Class<? extends DataPointTransformer>> datapointsClazz = reflections.getSubTypesOf(DataPointTransformer.class);
            Set<Class<? extends ControlPointTransformer>> controlpointsClazz = reflections.getSubTypesOf(ControlPointTransformer.class);
            Set<Class<? extends ExecutionEnvironmentTransformer>> executionEnvClazz = reflections.getSubTypesOf(ExecutionEnvironmentTransformer.class);
            Set<Class<? extends ConnectivityTransformater>> connectivityClazz = reflections.getSubTypesOf(ConnectivityTransformater.class);

            for (Class<? extends ProviderAdaptor> clazz : adaptorClasses) {
                adaptors.add(clazz.newInstance());
            }

            for (Class<? extends DataPointTransformer> clazz : datapointsClazz) {
                datapointTrans.add(clazz.newInstance());
            }

            for (Class<? extends ControlPointTransformer> clazz : controlpointsClazz) {
                controlPointTrans.add(clazz.newInstance());
            }

            for (Class<? extends ExecutionEnvironmentTransformer> clazz : executionEnvClazz) {
                executionEnvTrans.add(clazz.newInstance());
            }

            for (Class<? extends ConnectivityTransformater> clazz : connectivityClazz) {
                connectivityTrans.add(clazz.newInstance());
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error("Cannot instantiate the instance of a plugin");
            ex.printStackTrace();
        }

    }

    public DataPointTransformer getDatapointTransformerByName(String name) {
        for (DataPointTransformer a : datapointTrans) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }

    public ControlPointTransformer getControlpointTransformerByName(String name) {
        for (ControlPointTransformer a : controlPointTrans) {
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

    public ConnectivityTransformater getConnectivityTransformerByName(String name) {
        for (ConnectivityTransformater a : connectivityTrans) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }

    public Set<ProviderAdaptor> getAdaptors() {
        return adaptors;
    }

}
