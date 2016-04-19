/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DTOMapper.impl;

import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import sinc.hinc.repository.DTOMapper.DTOMapperInterface;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 *
 * @author hungld
 */
public class ConnectivityMapper implements DTOMapperInterface<CloudConnectivity> {

    @Override
    public CloudConnectivity fromODocument(ODocument doc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ODocument toODocument(CloudConnectivity object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
