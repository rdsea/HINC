/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.ResourceDriver;

import java.util.Map;
import sinc.hinc.abstraction.transformer.PhysicalResourceTransformer;

/**
 * This provider is for PUSH-BASE mechanism. The device will push to this
 * adaptor to update information. This should support device protocol like:
 * LWM2M, COAP, MQTT.
 *
 * @author hungld
 */
public interface ProviderListenerAdaptor {

    /**
     * The implementation of this interface should do.
     *
     * - Start a server for listening information.
     *
     * - Use "transformer" to convert the data into IoT Unit. Transformer is on
     * a separate class (e.g. on the same protocol MQTT, we can implement
     * different data model).
     *
     * - Use "process" to process the IoT Unit. Note: the process must be call
     * to inform HINC Local about the update.
     *
     * Note: this will be run by a thread, so no worry for thread block.
     *
     * @param settings he parameters from InfoSourceSettings, e.g. endpoint,
     * username, password. It is defined before HINC starts.
     * @param tranformer use this to trigger the transformer
     * @param process use this to inform HINC about the change
     */
    public void listen(Map<String, String> settings, PhysicalResourceTransformer tranformer, IoTUnitProcessor process);

    /**
     * Name is use to get the correct configuration in "source.conf" file.
     *
     * @return a String of the adaptor name.
     */
    public String getName();
    
}
