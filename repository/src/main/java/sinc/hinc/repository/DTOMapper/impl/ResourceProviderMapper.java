/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DTOMapper.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientechnologies.orient.core.record.impl.ODocument;
import java.util.List;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.IoTUnit;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;
import sinc.hinc.repository.DTOMapper.DTOMapperInterface;
import sinc.hinc.repository.Utils;

/**
 *
 * @author hungld
 */
public class ResourceProviderMapper implements DTOMapperInterface<ResourcesProvider> {

    @Override
    public ResourcesProvider fromODocument(ODocument doc) {
        ResourcesProvider rp = new ResourcesProvider();

        rp.setName(String.valueOf(doc.field("name")));
        rp.setUri(String.valueOf(doc.field("uri")));
        rp.setApis(Utils.jsonToListControlPoint(String.valueOf(doc.field("apis"))));
        rp.setSettings(Utils.jsonToMap(String.valueOf(doc.field("settings"))));
        return rp;
    }

    @Override
    public ODocument toODocument(ResourcesProvider object) {
        ODocument doc = new ODocument();
        doc.setClassName(ResourcesProvider.class.getSimpleName());
        doc.field("uuid", object.getUuid());
        doc.field("name", object.getName());
        doc.field("uri", object.getUri());
        doc.field("apis", Utils.ControlPointListToJson(object.getApis()));
        doc.field("settings", Utils.mapToJson(object.getSettings()));
        return doc;
    }

}
