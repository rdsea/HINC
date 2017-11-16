/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.ResourceDriver;

import sinc.hinc.abstraction.transformer.NetworkResourceTranformationInterface;

/**
 * This static class for quick support load the adaptor and transformer from the classpath.
 * @author hungld
 */
public class PluginFactory {

    public static ProviderQueryAdaptor<Object> getProviderAdaptor(String adaptorClass) {
        try {
            ProviderQueryAdaptor<Object> adaptor = (ProviderQueryAdaptor<Object>) Class.forName(adaptorClass).newInstance();
            return adaptor;
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
}
