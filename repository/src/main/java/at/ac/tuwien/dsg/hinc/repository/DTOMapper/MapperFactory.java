/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.repository.DTOMapper;

import at.ac.tuwien.dsg.hinc.communication.messagePayloads.HincMeta;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.Capability.Concrete.ExecutionEnvironment;
import at.ac.tuwien.dsg.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import at.ac.tuwien.dsg.hinc.model.VirtualNetworkResource.VNF;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.impl.ConnectivityMapper;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.impl.ControlPointMapper;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.impl.DataPointMapper;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.impl.ExecutionEnvironmentMapper;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.impl.HINCMetaMapper;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.impl.SoftwareDefinedGatewayMapper;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.impl.VNFMapper;

/**
 *
 * @author hungld
 */
public class MapperFactory {

    public static DTOMapperInterface getMapper(Class clazz) {
        String name = clazz.getSimpleName();
        System.out.println(name);

        if (clazz.equals(SoftwareDefinedGateway.class)) {
            return new SoftwareDefinedGatewayMapper();
        }

        if (clazz.equals(DataPoint.class)) {
            return new DataPointMapper();
        }

        if (clazz.equals(ControlPoint.class)) {
            return new ControlPointMapper();
        }

        if (clazz.equals(ExecutionEnvironment.class)) {
            return new ExecutionEnvironmentMapper();
        }

        if (clazz.equals(CloudConnectivity.class)) {
            return new ConnectivityMapper();
        }

        if (clazz.equals(VNF.class)) {
            return new VNFMapper();
        }

        if (clazz.equals(HincMeta.class)) {
            return new HINCMetaMapper();
        }

        System.out.println("Do not found the mapper to persist DB for: " + name);
        return null;

    }

}
