/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.VirtualComputingResource.Capabilities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import sinc.hinc.model.API.HINCPersistable;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;

/**
 *
 * @author hungld
 */
public class DataPoint implements HINCPersistable {

    // uuid and name for identification
    String uuid = UUID.randomUUID().toString();
    String name;

    // List of virtual resource this data point belongs to
    List<String> resourceUuid = new ArrayList<>();

    // Some data specific metadata
    // type: temperature, humidity, GPS, image, video_streaming, state
    // unit: degree, K, M
    Map<String, String> meta = new HashMap<>();

    /**
     * The dataApi define how to manage data. E.g: MQTTDataSource, FileScanner,
     * CommandLineParser, etc The parameters are provided by a Map of key,
     * value.
     */
    String dataApi;
    Map<String, String> dataApiSettings;

    /**
     * External relationship of the data point. E.g: which service is execute it
     */
    AccessPoint connectingTo;

    public DataPoint() {
    }

    public DataPoint(String name) {
        this.name = name;
    }

    @Override
    public String getUuid() {
        return this.uuid;
    }

    public String getDataApi() {
        return dataApi;
    }

    public void setDataApi(String dataApi) {
        this.dataApi = dataApi;
    }

    public Map<String, String> getDataApiSettings() {
        return dataApiSettings;
    }

    @JsonIgnore
    public String getDataAPISettingsPlain() {
        return dataApiSettings.toString();
    }

    public void setDataApiSettings(Map<String, String> dataApiSettings) {
        this.dataApiSettings = dataApiSettings;
    }

    public AccessPoint getConnectingTo() {
        return connectingTo;
    }

    public void setConnectingTo(AccessPoint connectingTo) {
        this.connectingTo = connectingTo;
    }

    public List<String> getResourceUuid() {
        return resourceUuid;
    }

    public void setResourceUuid(List<String> resourceUuid) {
        this.resourceUuid = resourceUuid;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.uuid);
        hash = 79 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataPoint other = (DataPoint) obj;
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

}
