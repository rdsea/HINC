package at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.controllers;

import at.ac.tuwien.dsg.hinc.globalmanagementservicespringboot.services.ResourceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sinc.hinc.common.model.Resource;
import sinc.hinc.common.model.ResourceProvider;
import sinc.hinc.common.model.capabilities.ControlPoint;
import sinc.hinc.common.model.capabilities.DataPoint;
import sinc.hinc.common.model.payloads.Control;
import sinc.hinc.common.model.payloads.ControlResult;

import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }


    @GetMapping
    public ResponseEntity<List<Resource>> getResources(@RequestParam(required = false, defaultValue = "0")Integer timeout,
                                                       @RequestParam(required = false, defaultValue = "")String id,
                                                       @RequestParam(required = false, defaultValue = "")String group,
                                                       @RequestParam(required = false, defaultValue = "0")Integer limit,
                                                       @RequestParam(required = false, defaultValue = "false")Boolean rescan) {

        List<Resource> resourceList = null;
        try {
            resourceList = resourceService.queryResources(timeout,id,group,limit,rescan);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resourceList, HttpStatus.OK);
    }




    /*
    Control {

    private String controlPointUuid;
    private String resourceProviderUuid;
    private String uuid;

    private String name;
    private JsonNode parameters;
    private ControlPoint.ControlType controlType;
    private Collection<AccessPoint> accessPoints = new ArrayList<>();
    private long timestamp;
     */

    /*@GetMapping("/{id}/controlpoints")
    public ResponseEntity<List<ControlPoint>> getControlPointsForResource(@PathVariable String id,
                                                                          @RequestParam(required = false, defaultValue = "0") Integer timeout,
                                                                          @RequestParam(required = false, defaultValue = "false") Boolean rescan){
        return null;
    }


    @GetMapping("/{id}/datapoints")
    public ResponseEntity<List<DataPoint>> getDataPointsForResource(@PathVariable String id,
                                                                    @RequestParam(required = false, defaultValue = "0") Integer timeout,
                                                                    @RequestParam(required = false, defaultValue = "false") Boolean rescan){
        return null;
    }*/



    /*
    GET /resources?type=&group=&id=&limit=&rescan=true&timeout=
    GET /resources/{id}/datapoints
    GET /resources/{id}/controlpoints
    POST /resources/{id}/controlpoints/{id}   Parameter:parameter




    GET /controlpoints
    POST /controlpoints/{id}   Parameter:parameter
    GET /datapoints
     */


    //@PostMapping("/control")
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
