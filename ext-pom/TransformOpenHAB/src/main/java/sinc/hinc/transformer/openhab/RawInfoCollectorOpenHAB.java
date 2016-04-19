/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.openhab;

import sinc.hinc.abstraction.ResourceDriver.InfoSourceSettings;

import java.util.Map;
import sinc.hinc.transformer.openhab.model.Item;
import sinc.hinc.transformer.openhab.model.Items;
import java.util.HashMap;
import sinc.hinc.abstraction.ResourceDriver.ProviderAdaptor;

/**
 * Adaptor for OpenHAB Require settings: - endpoint: to the REST API, e.g. http://localhost:8080/rest (without /)
 *
 * @author hungld
 */
public class RawInfoCollectorOpenHAB implements ProviderAdaptor {

    public RawInfoCollectorOpenHAB() {
    }

    @Override
    public Map<String, String> getRawInformation(InfoSourceSettings.InfoSource infoSource) {
        String endpoint = infoSource.getSettings().get("endpoint").trim();
        if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }

        HashMap<String, String> result = new HashMap<>();
        // read Item and add
        String itemListJson = RestHandler.callRest(endpoint + "/items", RestHandler.HttpVerb.GET, null, "*/*", "application/json");
//        System.out.println(itemListJson);
        Items items = new Items();
        items = (Items) items.readFromJson(itemListJson);
        for (Item i : items.getItem()) {
            result.put(i.getName(), i.writeToJson());
        }
        return result;
    }

    // generate the settings for this collector
    public static void main(String[] args) {
        RawInfoCollectorOpenHAB adaptor = new RawInfoCollectorOpenHAB();
        InfoSourceSettings settings = new InfoSourceSettings();
        InfoSourceSettings.InfoSource source = new InfoSourceSettings.InfoSource(InfoSourceSettings.InformationSourceType.OpenHAB, "at.ac.tuwien.dsg.hinc.transformopenhab.RawInfoCollectorOpenHAB", "at.ac.tuwien.dsg.hinc.transformopenhab.TranformOpenHABInfo");
        source.hasSetting("endpoint", "http://localhost:8080/rest");
        settings.getSource().add(source);

        System.out.println(settings.toJson());
        System.out.println(adaptor.getRawInformation(source).toString());
    }

}
