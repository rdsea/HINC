/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hungld
 */
public class DummyData {

    List<DummyMetadataItem> dataItems = new ArrayList<>();

    public DummyData(int numberOfItem) {
        // genetate 1000 datapoints
        for (int i = 0; i < numberOfItem; i++) {
            dataItems.add(new DummyMetadataItem(i+""));
        }

    }

    public DummyData() {
    }
    

    public void dynamicChange() {
        // TODO: a new thread to change the capability frequently
    }

    public List<DummyMetadataItem> getDataItems() {
        return dataItems;
    }
    
    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null; 
        }
    }

}
