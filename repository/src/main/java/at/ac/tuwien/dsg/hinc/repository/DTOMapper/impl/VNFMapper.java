/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.repository.DTOMapper.impl;

import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualNetworkResource.VNF;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.DTOMapperInterface;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 *
 * @author hungld
 */
public class VNFMapper implements DTOMapperInterface<VNF>{

    @Override
    public VNF fromODocument(ODocument doc) {
        VNF dp = new VNF();
        dp.setName(doc.field("name").toString());        
        return dp;
    }

    @Override
    public ODocument toODocument(VNF object) {
        ODocument doc = new ODocument("VNF");
        doc.field("name", object.getName());        
        return doc;
    }
    
}
