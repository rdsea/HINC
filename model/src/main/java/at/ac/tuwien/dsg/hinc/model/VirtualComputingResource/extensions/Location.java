/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.extensions;

import at.ac.tuwien.dsg.hinc.model.PhysicalResource.ExtensibleModel;

/**
 *
 * @author hungld
 */
public class Location extends ExtensibleModel{

    String latitude;
    String longitude;
    String speed;

    public Location(String latitude, String longitude, String speed) {
        super(Location.class);
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

}
