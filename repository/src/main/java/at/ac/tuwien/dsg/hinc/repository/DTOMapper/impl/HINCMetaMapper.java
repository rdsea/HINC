/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.repository.DTOMapper.impl;

import at.ac.tuwien.dsg.hinc.communication.messagePayloads.HincMeta;
import at.ac.tuwien.dsg.hinc.repository.DTOMapper.DTOMapperInterface;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 *
 * @author hungld
 */
public class HINCMetaMapper implements DTOMapperInterface<HincMeta> {

    @Override
    public HincMeta fromODocument(ODocument doc) {
        HincMeta meta = new HincMeta();
        meta.setIp(doc.field("ip").toString());
        meta.setSettings(doc.field("settings").toString());
        meta.setUnicastTopic(doc.field("unicasttopic").toString());
        meta.setUuid(doc.field("uuid").toString());
        return meta;
    }

    @Override
    public ODocument toODocument(HincMeta object) {
        ODocument doc = new ODocument("HINCMeta");
        doc.field("ip", object.getIp());
        doc.field("settings", object.getSettings());
        doc.field("unicasttopic", object.getUnicastTopic());
        doc.field("uuid", object.getUuid());
        return doc;
    }

}
