package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.controllers;

import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.services.ResourceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.model.ResourceProvider;
import sinc.hinc.common.model.capabilities.ControlPoint;
import sinc.hinc.common.model.capabilities.DataPoint;
import sinc.hinc.common.model.payloads.ControlResult;

import java.util.List;

@RestController
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }


    @GetMapping("/resources")
    public ResponseEntity<List<Resource>> queryResources(int timeout, String id, String group, int limit, boolean rescan) {

        List<Resource> resourceList = null;
        try {
            resourceList = resourceService.queryResources(timeout,id,group,limit,rescan);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resourceList, HttpStatus.OK);
    }


    @GetMapping("/resourceproviders")
    public ResponseEntity<List<ResourceProvider>> queryResourceProviders(int timeout, String id, String group, int limit, boolean forceRescan) {
        //TODO Broadcast QUERY_IOT_PROVIDERS (SenderID:UUID of Global, ResponseTopic: Temporary)
        //HincMessage queryMessage = new HincMessage(HINCMessageType.QUERY_IOT_PROVIDERS.toString(), HincConfiguration.getMyUUID(), HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName()), feedBackTopic, payload);

        return null;
    }

    @PostMapping("/control")
    public ResponseEntity<ControlResult> sendControl(String gatewayid, String resourceid, String actionName, String param) {
        //TODO Groupcast/Broadcast CONTROL (SenderID:UUID of Global, ResponseTopic: Temporary)
        /*HincMessage controlPointRequest = new HincMessage(HINCMessageType.CONTROL.toString(), HincConfiguration.getMyUUID(), HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName()), HincMessageTopic.getTemporaryTopic(), controlPointUUID);
        controlPointRequest.hasExtra("param", param);*/

        return null;
    }


    public ResponseEntity<List<DataPoint>> queryDataPoint(int timeout, String infoBases, String hincUUID) {
        return null;
    }
    public ResponseEntity<List<ControlPoint>> queryControlPoint(int timeout, String infoBases, String hincUUID) {
        return null;
    }

    public ResponseEntity<List<Resource>> queryIotResource(int timeout, String hincUUID, String infoBases, int limit, String rescan) {
        return null;
    }
    public ResponseEntity<List<Resource>> queryNetworkFunctionService(int timeout, String hincUUID, String infoBases, int limit, String rescan) {
        return null;
    }
    public ResponseEntity<List<Resource>> queryCloudService(int timeout, String hincUUID, String infoBases, int limit, String rescan) {
        return null;
    }




}
