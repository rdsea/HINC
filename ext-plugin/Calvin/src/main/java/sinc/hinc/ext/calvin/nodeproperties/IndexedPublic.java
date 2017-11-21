/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.ext.calvin.nodeproperties;

import java.util.Map;

/**
 *
 * @author hungld
 */
public class IndexedPublic {

    Owner owner;
    Address address;
    NodeName node_name;
    Map<String, String> user_extra;

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public NodeName getNode_name() {
        return node_name;
    }

    public void setNode_name(NodeName node_name) {
        this.node_name = node_name;
    }

    public Map<String, String> getUser_extra() {
        return user_extra;
    }

    public void setUser_extra(Map<String, String> user_extra) {
        this.user_extra = user_extra;
    }

}
