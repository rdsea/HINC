/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.transformer.openhab.model;

import sinc.hinc.model.PhysicalResource.ExtensibleModel;
import java.util.List;

/**
 *
 * @author hungld
 */
public class Items extends ExtensibleModel {

    List<Item> item;

    public Items() {
        super(Items.class);
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

}
