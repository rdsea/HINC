/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.ResourceDriver;


import sinc.hinc.abstraction.transformer.CloudResourceTransformation;
import sinc.hinc.abstraction.transformer.IoTResourceTransformation;
import sinc.hinc.abstraction.transformer.NetworkResourceTranformationInterface;

/**
 * This static class for quick support load the adaptor and transformer from the classpath.
 * @author hungld
 */
public class PluginFactory {

    public static ProviderAdaptor<Object> getProviderAdaptor(String adaptorClass) {
        try {
            ProviderAdaptor<Object> adaptor = (ProviderAdaptor<Object>) Class.forName(adaptorClass).newInstance();
            return adaptor;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static IoTResourceTransformation<Object> getIoTResourceTransformer(String transformerClass) {
        try {
            IoTResourceTransformation<Object> transformer = (IoTResourceTransformation<Object>) Class.forName(transformerClass).newInstance();
            return transformer;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static NetworkResourceTranformationInterface<Object> getNetworkResourceTransformer(String transformerClass) {
        try {
            NetworkResourceTranformationInterface<Object> transformer = (NetworkResourceTranformationInterface<Object>) Class.forName(transformerClass).newInstance();
            return transformer;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static CloudResourceTransformation<Object> getCloudResourceTransformer(String transformerClass) {
        try {
            CloudResourceTransformation<Object> transformer = (CloudResourceTransformation<Object>) Class.forName(transformerClass).newInstance();
            return transformer;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
