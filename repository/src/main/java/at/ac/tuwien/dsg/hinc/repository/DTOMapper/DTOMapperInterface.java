/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.repository.DTOMapper;

import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 *
 * @author hungld
 * @param <T> The class to be converted
 */
public interface DTOMapperInterface<T> {

    T fromODocument(ODocument doc);

    ODocument toODocument(T object);

}
