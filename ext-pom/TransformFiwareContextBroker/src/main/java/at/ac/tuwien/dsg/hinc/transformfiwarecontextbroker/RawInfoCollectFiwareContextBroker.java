/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.transformfiwarecontextbroker;

import at.ac.tuwien.dsg.hinc.abstraction.ResourceDriver.InfoSourceSettings;
import at.ac.tuwien.dsg.hinc.abstraction.ResourceDriver.ProviderAdaptor;
import at.ac.tuwien.dsg.hinc.transformfiwarecontextbroker.model.Attribute;
import at.ac.tuwien.dsg.hinc.transformfiwarecontextbroker.model.ContextElement;
import at.ac.tuwien.dsg.hinc.transformfiwarecontextbroker.model.ContextResponse;
import at.ac.tuwien.dsg.hinc.transformfiwarecontextbroker.model.ContextResponseWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hungld
 */
public class RawInfoCollectFiwareContextBroker implements ProviderAdaptor {

    @Override
    public Map<String, String> getRawInformation(InfoSourceSettings.InfoSource infoSource) {
        HashMap<String, String> result = new HashMap<>();
        String endpoint = infoSource.getSettings().get("endpoint").trim();
        if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }

        // hack a bit: only query room data
        String fullQuery = "(curl localhost:1026/v1/queryContext?limit=2 -s -S --header 'Content-Type: application/json' --header 'Accept: application/json' -d @- | python -mjson.tool) <<EOF { \"entities\": [ { \"type\": \"Room\", \"isPattern\": \"true\", \"id\": \"room.*\" } ] } EOF";
        String json = executeCommand(fullQuery);
        ContextResponseWrapper contextWrapper = ContextResponseWrapper.fromJson(json);
        for (ContextResponse contextRes : contextWrapper.getContextResponses()) {
            ContextElement element = contextRes.getContextElement();
            for (Attribute attr : element.getAttributes()) {
                String id = element.getId()+"."+attr.getName();
                ContextElement newElement = new ContextElement(); // this contain 1 attributes
                newElement.setId(element.getId());
                newElement.setAttributes(Arrays.asList(attr));
                result.put(id, newElement.toJson());
            }
        }
        return result;
    }

    String executeCommand(String command) {
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);

            p.waitFor();

            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (IOException ex) {
            return null;
        } catch (InterruptedException ex) {
            return null;
        }
    }

}
