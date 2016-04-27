/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.transformer;


import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ExecutionEnvironment;
import java.util.List;

/**
 * The transformer extract the capabilities from the DomainModel to the HINC capabilities.
 * The DomainModel of this interface much be match with the domain model of the Adaptor.
 * As describe in the ProviderAdaptor interface, the DomainModel represents a single items.
 * 
 * TODO: check if the DomainModel are the same with Adaptor.
 * 
 * @author hungld
 * @param <DomainModel> the class model which is transformed to the model
 */
public interface IoTResourceTransformation<DomainModel> {
    /**
     * For each items, this extracts one data point
     * @param data Information item captured from provider
     * @return A single data point.
     */
    public DataPoint updateDataPoint(DomainModel data);
    
    /**
     * For each datapoint or information items of provider, this extracts several control. E.g. One data point can be control by several actions.
     * @param data Information item captured from provider
     * @return 
     */
    public List<ControlPoint> updateControlPoint(DomainModel data);
    
    /**
     * How the device supports deployment of other components.
     * @param data Information item captured from provider.
     * @return The description of environment.
     */
    public ExecutionEnvironment updateExecutionEnvironment(DomainModel data);
    
    /**
     * How the data can be routed via different network.
     * @param data Information item captured from provider
     * @return The description of connectivity that the provider supports for such item. E.g. the data of this provider can forward to different network.
     */
    public CloudConnectivity updateCloudConnectivity(DomainModel data);

}
