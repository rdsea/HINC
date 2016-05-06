/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DTOMapper;

import sinc.hinc.common.metadata.HincLocalMeta;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.CloudConnectivity;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.DataPoint;
import sinc.hinc.model.VirtualComputingResource.Capability.Concrete.ExecutionEnvironment;
import sinc.hinc.model.VirtualComputingResource.SoftwareDefinedGateway;
import sinc.hinc.model.VirtualNetworkResource.VNF;
import sinc.hinc.repository.DTOMapper.impl.ConnectivityMapper;
import sinc.hinc.repository.DTOMapper.impl.ControlPointMapper;
import sinc.hinc.repository.DTOMapper.impl.DataPointMapper;
import sinc.hinc.repository.DTOMapper.impl.ExecutionEnvironmentMapper;
import sinc.hinc.repository.DTOMapper.impl.HINCMetaMapper;
import sinc.hinc.repository.DTOMapper.impl.SoftwareDefinedGatewayMapper;
import sinc.hinc.repository.DTOMapper.impl.VNFMapper;

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

        if (clazz.equals(HincLocalMeta.class)) {
            return new HINCMetaMapper();
        }

        System.out.println("Do not found the mapper to persist DB for: " + name);
        return null;

    }

}
