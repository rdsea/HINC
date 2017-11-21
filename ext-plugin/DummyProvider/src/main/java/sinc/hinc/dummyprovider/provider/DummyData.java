/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hungld
 */
public class DummyData {

    static Logger logger = LoggerFactory.getLogger("Dummy");
    List<DummyMetadataItem> dataItems = new ArrayList<>();

    // the buffer is used in some algorithms (e.g. sensor are disable, disappear, error, turnoff, etc.)
    Queue<DummyMetadataItem> buffers = new ArrayDeque();

    public DummyData(int numberOfItem) {
        // generate some datapoints
        logger.debug("Start new DummyData, generating " + numberOfItem + " data items..");
        for (int i = 0; i < numberOfItem; i++) {
            dataItems.add(new DummyMetadataItem(i + ""));
        }
    }

    public DummyData() {
    }

    public List<DummyMetadataItem> getDataItems() {
        return dataItems;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    public Queue<DummyMetadataItem> getBuffers() {
        return buffers;
    }

    public void setBuffers(Queue<DummyMetadataItem> buffers) {
        this.buffers = buffers;
    }

}
