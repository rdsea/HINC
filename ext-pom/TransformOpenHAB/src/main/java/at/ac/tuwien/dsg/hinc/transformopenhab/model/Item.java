/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.transformopenhab.model;

import at.ac.tuwien.dsg.hinc.model.PhysicalResource.ExtensibleModel;

/**
 *
 * @author hungld
 */
public class Item extends ExtensibleModel{
    String type;
    String name;
    String state;
    String link;

    public Item() {
        super(Item.class);
    }

    public Item(String type, String name, String state, String link) {
        super(Item.class);
        this.type = type;
        this.name = name;
        this.state = state;
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    
}
