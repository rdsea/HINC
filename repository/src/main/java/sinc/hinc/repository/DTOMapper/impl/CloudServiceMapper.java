package sinc.hinc.repository.DTOMapper.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientechnologies.orient.core.record.impl.ODocument;
import sinc.hinc.model.CloudServices.CloudService;
import sinc.hinc.model.VirtualNetworkResource.AccessPoint;
import sinc.hinc.model.VirtualNetworkResource.NetworkFunctionService;
import sinc.hinc.repository.DTOMapper.DTOMapperInterface;
import sinc.hinc.repository.Utils;

public class CloudServiceMapper implements DTOMapperInterface<CloudService> {
    @Override
    public CloudService fromODocument(ODocument doc) {
        CloudService cloudService = new CloudService();
        cloudService.setType(String.valueOf(doc.field("type")));
        cloudService.setHostedOnUUID(String.valueOf(doc.field("hostedonuuid")));
        cloudService.setProviderUUID(String.valueOf(doc.field("provideruuid")));
        cloudService.setAttributes(Utils.jsonToMap(String.valueOf(doc.field("attributes"))));
        cloudService.setUuid(String.valueOf(doc.field("uuid")));
        cloudService.setAccessPoint(new AccessPoint(String.valueOf(doc.field("accesspoint"))));
        return cloudService;
    }

    @Override
    public ODocument toODocument(CloudService cloudService) {
        ODocument document = new ODocument(CloudService.class.getSimpleName());
        document.field("type", cloudService.getType());
        document.field("hostedonuuid", cloudService.getHostedOnUUID());
        document.field("provideruuid", cloudService.getProviderUUID());
        if(cloudService.getAccessPoint() != null){
            document.field("accesspointendpoint", cloudService.getAccessPoint().getEndpoint());
        }else{
            document.field("accesspointendpoint");
        }

        document.field("uuid", cloudService.getUuid());

        document.field("attributes",Utils.mapToJson(cloudService.getAttributes()));
        return document;
    }
}
