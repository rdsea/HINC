/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.fiwarecontextbroker;

import sinc.hinc.abstraction.ResourceDriver.ProviderAdaptor;
import sinc.hinc.transformer.fiwarecontextbroker.model.Attribute;
import sinc.hinc.transformer.fiwarecontextbroker.model.ContextElement;
import sinc.hinc.transformer.fiwarecontextbroker.model.ContextResponse;
import sinc.hinc.transformer.fiwarecontextbroker.model.ContextResponseWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author hungld
 */
public class RawInfoCollectFiwareContextBroker implements ProviderAdaptor<ContextElement> {

    @Override
    public Collection<ContextElement> getItems(Map<String, String> settings) {
        Collection<ContextElement> result = new ArrayList<>();
        String endpoint = settings.get("endpoint").trim();
        if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }

        // hack a bit: only query room data. Should be extended later
        String fullQuery = "(curl localhost:1026/v1/queryContext?limit=2 -s -S --header 'Content-Type: application/json' --header 'Accept: application/json' -d @- | python -mjson.tool) <<EOF { \"entities\": [ { \"type\": \"Room\", \"isPattern\": \"true\", \"id\": \"room.*\" } ] } EOF";
        String json = executeCommand(fullQuery);
        ContextResponseWrapper contextWrapper = ContextResponseWrapper.fromJson(json);
        for (ContextResponse contextRes : contextWrapper.getContextResponses()) {
            ContextElement element = contextRes.getContextElement();
            for (Attribute attr : element.getAttributes()) {
                String id = element.getId() + "." + attr.getName();
                ContextElement newElement = new ContextElement(); // this contain 1 attributes
                newElement.setId(element.getId());
                newElement.setAttributes(Arrays.asList(attr));
                result.add(newElement);
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

    @Override
    public void sendControl(String controlAction, Map parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getName() {
        return "fiwarecontextbroker";
    }

}
