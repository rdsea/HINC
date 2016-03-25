/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.transformer.iotivity;

/**
 *
 * @author hungld
 */
public class Core {

    String type;
    String interfaceSet;
    Integer observable;
    String name;
    String id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInterfaceSet() {
        return interfaceSet;
    }

    public void setInterfaceSet(String interfaceSet) {
        this.interfaceSet = interfaceSet;
    }

    public Integer getObservable() {
        return observable;
    }

    public void setObservable(Integer observable) {
        this.observable = observable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
