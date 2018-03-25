package model;

import java.util.HashMap;
import java.util.Map;

public class DatasetConfig {
    private Dataset sampleConfig;

    public Dataset getSampleConfig() {
        return sampleConfig;
    }

    public void setSampleConfig(Dataset sampleConfig) {
        this.sampleConfig = sampleConfig;
    }

    public Map<String, String> asMap(){
        Map<String, String> map = new HashMap<>();
        map.put("datasetId", sampleConfig.getDatasetId());


        return map;
    }
}
