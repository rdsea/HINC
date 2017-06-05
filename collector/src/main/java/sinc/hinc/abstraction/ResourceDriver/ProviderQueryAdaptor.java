/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.abstraction.ResourceDriver;

import java.util.Collection;
import java.util.Map;

/**
 * The provider adaptor is used by Local Management Service. This provide is
 * for: PULL-BASE QUERY It do 2 things: query information and send a control The
 * implementation of this interface must interact with specific the provider
 * API.
 *
 * @author hungld
 * @param <DomainClass> The domain model of the provider support. The
 * DomainClass represents single items, in order to transform. E.g. a provider
 * return a set of asset, this class must attract to individual items to process
 * later. The <DomainClass> must be matched with the DomainClass of the
 * Transformation.
 */
public interface ProviderQueryAdaptor<DomainClass> {

    /**
     * This for invoking REST API of a provider and to get a list of provider
     * specific items. E.g., A set of of sensor metadata This function should do
     * following:
     *
     * <p>
     * <ol>
     * <li>Understand the context of provider, e.g. where is the API, how to
     * access. This is set in the settings as input parameter.
     * <li>Invoke the API and understand the result
     * <li>From the result, extract information into individual items. E.g.
     * provider can return a set or composite format, but we need to process
     * information in items. A item should be a sensor or actuator (not the
     * composite one)
     * </ol><p>
     *
     * @param settings the parameters from InfoSourceSettings, e.g. endpoint,
     * username, password. It is defined before HINC starts.
     * @return a collection of items.
     */
    public Collection<DomainClass> getItems(Map<String, String> settings);

    /**
     * This is for sending a control to a provider. The implementation of this
     * method must do following: 1. Translate the controlAction and parameters
     * into provider API call 2. The execution of the command can reuse some
     * default HINC functions, e.g. RESTManager
     *
     * @param controlAction the
     * @param parameters
     */
    public void sendControl(String controlAction, Map<String, String> parameters);

    /**
     * A readable name to enable external configuration, e.g. read "source.conf"
     * Name should not contain spaces.
     * @return 
     */
    public String getName();

}
