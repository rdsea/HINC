/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.repository.DTOMapper;

import at.ac.tuwien.dsg.hinc.repository.DTOMapper.DTOMapperInterface;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.impl.DataPointMapper;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.impl.SoftwareDefinedGatewayMapper;
import java.lang.reflect.ParameterizedType;

/**
 *
 * @author hungld
 */
public class MapperFactory {

    public static DTOMapperInterface getMapper(Class clazz) {
        String name = clazz.getSimpleName();
        System.out.println(name);
        switch (name) {
            case "SoftwareDefinedGateway":
                return new SoftwareDefinedGatewayMapper();
            case "DataPoint":
                return new DataPointMapper();
            default:
                return null;
        }
    }

}
