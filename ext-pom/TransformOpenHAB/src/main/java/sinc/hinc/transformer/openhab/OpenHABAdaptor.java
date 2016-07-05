/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.openhab;

import java.util.ArrayList;
import java.util.Collection;

import sinc.hinc.transformer.openhab.model.Item;
import sinc.hinc.transformer.openhab.model.Items;
import java.util.Map;
import sinc.hinc.abstraction.ResourceDriver.ProviderAdaptor;

/**
 * Adaptor for OpenHAB Require settings.
 * <p>
 * endpoint: to the REST API. The default should be: http://localhost:8080/rest
 * (without /)
 *
 * @author hungld
 */
public class OpenHABAdaptor implements ProviderAdaptor<Item> {

    @Override
    public Collection<Item> getItems(Map<String, String> settings) {
        String endpoint = settings.get("endpoint").trim();
        if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }

        Collection<Item> result = new ArrayList<>();
        // read Item and add
        String itemListJson = RestHandler.callRest(endpoint + "/items", RestHandler.HttpVerb.GET, null, "*/*", "application/json");
//        System.out.println(itemListJson);
        Items items = new Items();
        items = (Items) items.readFromJson(itemListJson);
        return items.getItem();
    }

    @Override
    public void sendControl(String controlAction, Map<String, String> parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return "openhab";
    }

}
