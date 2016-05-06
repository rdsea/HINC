/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.repository.DTOMapper.impl;


import sinc.hinc.repository.DTOMapper.DTOMapperInterface;
import com.orientechnologies.orient.core.record.impl.ODocument;
import sinc.hinc.common.metadata.HincLocalMeta;

/**
 *
 * @author hungld
 */
public class HINCMetaMapper implements DTOMapperInterface<HincLocalMeta> {

    @Override
    public HincLocalMeta fromODocument(ODocument doc) {
        HincLocalMeta meta = new HincLocalMeta();
        meta.setIp(String.valueOf(doc.field("ip")));
        meta.setSettings(String.valueOf(doc.field("settings")));
        meta.setUnicastTopic(String.valueOf(doc.field("unicasttopic")));
        meta.setUuid(String.valueOf(doc.field("uuid")));
        return meta;
    }

    @Override
    public ODocument toODocument(HincLocalMeta object) {
        ODocument doc = new ODocument(HincLocalMeta.class.getSimpleName());
        doc.field("ip", object.getIp());
        doc.field("settings", object.getSettings());
        doc.field("unicasttopic", object.getUnicastTopic());
        doc.field("uuid", object.getUuid());
        return doc;
    }

}
