/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DTOMapper.impl;

import sinc.hinc.repository.DTOMapper.DTOMapperInterface;
import com.orientechnologies.orient.core.record.impl.ODocument;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ExecutionEnvironment;

/**
 *
 * @author hungld
 */
public class ExecutionEnvironmentMapper implements DTOMapperInterface<ExecutionEnvironment> {

    @Override
    public ExecutionEnvironment fromODocument(ODocument doc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ODocument toODocument(ExecutionEnvironment object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
