/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.model.Extensible.facility;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import sinc.hinc.model.Extensible.ExtensibleModel;

/**
 *
 * @author hungld
 */
public class Facility extends ExtensibleModel {

    // the short name for the component, e.g. room 201
    String name;
    // the hierachical name, e.g. building1.floor2.room201
    String hierachicalName;

    String type;

    // any external model for the location, can be GPS, or other hierachical
    // by default, the FacilityComponent will derived the location from its father unless reset
    Object anyLocation;

    // to traverse and query
    Facility parent;
    List<Facility> children;

    public Facility() {
        super(Facility.class);
    }

    public Facility(String name, String type, Facility parent) {
        super(Facility.class);
        this.name = name;
        this.type = type;
        this.parent = parent;
        if (parent != null) {
            this.hierachicalName = parent.getName() + "." + name;
        }
    }

    public Facility(String name, FacilityType type) {
        super(Facility.class);
        this.name = name;
        this.type = type.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHierachicalName() {
        return hierachicalName;
    }

    public void setHierachicalName(String hierachicalName) {
        this.hierachicalName = hierachicalName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getAnyLocation() {
        return anyLocation;
    }

    public void setAnyLocation(Object anyLocation) {
        this.anyLocation = anyLocation;
    }

    @JsonIgnore
    public Facility getParent() {
        return parent;
    }

    public void setParent(Facility parent) {
        this.parent = parent;
    }

    public List<Facility> getChildren() {
        return children;
    }

    public Facility hasChildren(Facility children) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        children.parent = this;        
        this.children.add(children);
        return this;
    }
    
    

}
