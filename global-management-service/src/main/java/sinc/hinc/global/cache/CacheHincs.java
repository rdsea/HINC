/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.global.cache;

import sinc.hinc.communication.messagePayloads.HincMeta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hungld
 */
public class CacheHincs extends Cache {

    List<HincMeta> gateways = new ArrayList<>();

    public static CacheHincs newInstance() {
        return new CacheHincs();
    }

    public CacheHincs() {
        super(Cache.CacheInfo.delise);
    }

    public List<HincMeta> loadDelisesCache() {
        ObjectMapper mapper = new ObjectMapper();
        if (!(new File(getFileName())).exists()) { // file is not existing
            return null;
        }
        try {

            CacheHincs cache = mapper.readValue(new File(getFileName()), CacheHincs.class);
            return cache.gateways;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void writeDeliseCache(List<HincMeta> gateways) {
        this.gateways.addAll(gateways);
        System.out.println("writng delise list: " + this.gateways.size() + " items");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);

            System.out.println("Json: " + json);

            writeStringToFile(json, getFileName());
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<HincMeta> getGateways() {
        return gateways;
    }

    public void setGateways(List<HincMeta> gateways) {
        this.gateways = gateways;
    }

}
