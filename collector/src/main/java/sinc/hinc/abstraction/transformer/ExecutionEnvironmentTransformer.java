/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.transformer;

import sinc.hinc.model.VirtualComputingResource.Capabilities.ExecutionEnvironment;

/**
 *
 * @author hungld
 */
public interface ExecutionEnvironmentTransformer<DomainModel> {

    /**
     * How the device supports deployment of other components.
     *
     * @param data Information item captured from provider.
     * @return The description of environment.
     */
    public ExecutionEnvironment updateExecutionEnvironment(DomainModel data);

    public String getName();
}
